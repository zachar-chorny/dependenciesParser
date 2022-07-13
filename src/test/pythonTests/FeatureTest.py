import json
import requests
from requests.structures import CaseInsensitiveDict


class TestParse:
    APPLICATION_URL = "http://localhost:8080/project/create"

    def test_data_without_path(self):
        data = '{"path" : null}'
        response = self.get_response(data)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Path can\'t be null'

    def test_instruction_without_name(self):
        data = '{"path" : "main.py", "instructions" : [{"name":null}]}'
        response = self.get_response(data)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Name can\'t be null'

    def test_correct_data(self):
        data = '{"path" : "/Users/zchornyi/IdeaProjects/dependencie/pom.xml"}'
        response = self.get_response(data)
        actual_result = json.loads(response.content)
        expected_result = self.get_json_from_file("files/responses/defaultResult.json")
        assert expected_result == actual_result

    def test_data_with_new_project(self):
        data = '{"path":"/Users/zchornyi/IdeaProjects/dependenciesParserSecondProject/src/main/resources/files/pom' \
               '.xml","instructions":[{"name":"Parse","nodesForAdding": [{"groupId":"org.agiso.core",' \
               '"artifactId":"agiso-core-i18n","version":"0.0.2.RELEASE","type":"jar","scope":"compile"}],' \
               '"artifactIdsForRemoving":["aether-api", "aether-spi", "aether-util"],"nodesFroReplacing":[{' \
               '"groupId":"org.jboss.shrinkwrap.resolver","artifactId":"shrinkwrap-resolver-impl-maven",' \
               '"version":"3.1.4","type":"jar","scope":"test"}]}]} '
        response = self.get_response(data)
        actual_result = json.loads(response.content)
        expected_result = self.get_json_from_file("files/responses/resultWithNewProject.json")
        assert expected_result == actual_result

    def get_json_from_file(self, path):
        with open(path) as jsonFile:
            json_object = json.load(jsonFile)
            jsonFile.close()
        return json_object

    def get_response(self, data):
        headers = CaseInsensitiveDict()
        headers["Content-Type"] = "application/json"
        return requests.post(self.APPLICATION_URL, headers=headers, data=data)


class JsonResponse(object):
    def __init__(self, message, httpStatus):
        self.message = message
        self.httpStatus = httpStatus
