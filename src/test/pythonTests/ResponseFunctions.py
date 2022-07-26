import json
import requests
from requests.structures import CaseInsensitiveDict


class Response:
    @staticmethod
    def get_json_from_file(path):
        with open(path) as jsonFile:
            json_object = json.load(jsonFile)
            jsonFile.close()
        return json_object

    @staticmethod
    def get_response(data, url):
        headers = CaseInsensitiveDict()
        headers["Content-Type"] = "application/json"
        return requests.post(url, headers=headers, data=data)


class JsonResponse(object):
    def __init__(self, message, httpStatus):
        self.message = message
        self.httpStatus = httpStatus