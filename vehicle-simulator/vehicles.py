import argparse
import logging
import math
import random
import time

import requests


logging.basicConfig(
    datefmt='%Y-%m-%d %H:%M:%S',
    format='%(asctime)s %(levelname)s [%(filename)s:%(lineno)d - %(funcName)s] %(message)s',
    level=logging.INFO
)


EARTH_RADIUS = 6373.0
LOGGER = logging.getLogger(__name__)


def cli():
    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument('host', type=str)
    arg_parser.add_argument('port', type=int)
    arg_parser.add_argument('--num', type=int, default=10)
    arg_parser.add_argument('--lat', type=float, default=47.47581)
    arg_parser.add_argument('--lon', type=float, default=19.05749)
    return arg_parser


class Vehicle:
    def __init__(self, host, port, latitude, longitude):
        self.latitude = latitude
        self.longitude = longitude

        self.id = None

        self.base_url = f'http://{host}:{port}'

        self.last_movement_change_at = time.time()
        self.next_movement_change_at = self.last_movement_change_at

        self.speed = 0
        self.heading = 0

        self.prefix = None

    def register(self):
        res = requests.post(f'{self.base_url}/vehicles', json={})

        if res.status_code != 200:
            LOGGER.error(f'{self.prefix} Expected HTTP/200, got HTTP/{res.status_code}')
            return

        json = res.json()

        if 'id' not in json:
            LOGGER.error(f'{self.prefix} Invalid response: "id" not found')
            return

        self.id = json['id']
        self.prefix = f'[vehicle-{self.id}]'

    def move(self):
        if self.last_movement_change_at >= self.next_movement_change_at:
            self.last_movement_change_at = time.time()
            self.next_movement_change_at = self._random_next_movement_change_time()
            self.speed = Vehicle._random_speed()
            self.heading = Vehicle._random_heading()

            LOGGER.debug(f'{self.prefix} Update random movement: speed={self.speed} (m/s), heading={self.heading} (deg)')

        self.next_position()

        LOGGER.debug(f'{self.prefix} Current position: {self.latitude}, {self.longitude}')

        data = {
            'latitude': self.latitude,
            'longitude': self.longitude
        }

        res = requests.post(f'{self.base_url}/vehicle/{self.id}', json=data)

        if res.status_code != 200:
            LOGGER.error(f'{self.prefix} Expected HTTP/200, got HTTP/{res.status_code}')

    def query_vehicles_in_range(self):
        params = {
            'latitude': self.latitude,
            'longitude': self.longitude,
            'radius': 200
        }

        res = requests.get(f'{self.base_url}/vehicles', params=params)

        if res.status_code != 200:
            LOGGER.error(f'{self.prefix} Expected HTTP/200, got HTTP/{res.status_code}')
            return 0

        json = res.json()

        if 'vehicles' not in json:
            LOGGER.error(f'{self.prefix} Invalid response: "vehicles" not found')
            return 0

        if not isinstance(json['vehicles'], list):
            LOGGER.error(f'{self.prefix} Invalid response: "vehicles" is not a JSON array')
            return 0

        num_vehicles_in_range = len(json['vehicles'])

        LOGGER.info(f'{self.prefix} Number of vehicles in range: {num_vehicles_in_range}')

        return num_vehicles_in_range

    def next_position(self):
        distance = 0.1 * self.speed / 1000 / EARTH_RADIUS

        latitude = math.radians(self.latitude)
        longitude = math.radians(self.longitude)

        new_latitude = math.asin(math.sin(latitude) * math.cos(distance) +
                                 math.cos(latitude) * math.sin(distance) * math.cos(self.heading))
        new_longitude = longitude + math.atan2(math.sin(self.heading) * math.sin(distance) * math.cos(latitude),
                                               math.cos(distance) - math.sin(latitude) * math.sin(new_latitude))
        new_longitude = (new_longitude + 3 * math.pi) % (2 * math.pi) - math.pi

        self.latitude, self.longitude = math.degrees(new_latitude), math.degrees(new_longitude)

    def send_notification(self, num_vehicles_in_range):
        data = {
            'vehicle_id': self.id,
            'message': f'{num_vehicles_in_range} vehicles in range'
        }

        res = requests.post(f'{self.base_url}/notifications', json=data)

        LOGGER.info(f'{self.prefix} Sent notification with data: {data}')

        if res.status_code != 200:
            LOGGER.error(f'{self.prefix} Expected HTTP/200, got HTTP/{res.status_code}')

    def _random_next_movement_change_time(self):
        return self.last_movement_change_at + random.randrange(5, 15)

    @staticmethod
    def _random_speed():
        return random.randrange(5, 50) / 3.6

    @staticmethod
    def _random_heading():
        return math.radians(random.randrange(0, 360))


if __name__ == '__main__':
    args = cli().parse_args()
    vehicles = [Vehicle(args.host, args.port, args.lat, args.lon)
                for _ in range(args.num)]

    try:
        while True:
            for vehicle in vehicles:
                if vehicle.id is None:
                    vehicle.register()
                else:
                    vehicle.move()
                    num_vehicles_in_range = vehicle.query_vehicles_in_range()

                    if num_vehicles_in_range > 0:
                        vehicle.send_notification(num_vehicles_in_range)

            time.sleep(0.1)
    except KeyboardInterrupt:
        LOGGER.info('Exited')
