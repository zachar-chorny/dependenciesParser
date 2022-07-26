import json

from ResponseFunctions import Response
from ResponseFunctions import JsonResponse


class TestChange:
    APPLICATION_URL = "http://localhost:8080/changes/get"

    def test_data_without_path(self):
        data = '{"path" : null}'
        response = Response.get_response(data, self.APPLICATION_URL)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Path can\'t be null'

    def test_request_without_instruction(self):
        data = '{"path" : "main.py", "instructions" : null}'
        response = Response.get_response(data, self.APPLICATION_URL)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content)
        assert json_response.httpStatus == 400
        assert json_response.message == 'Instructions can\'t be null'

    def test_instruction_without_name(self):
        data = '{"path" : "main.py", "instructions" : [{"name":null}]}'
        response = Response.get_response(data, self.APPLICATION_URL)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Name can\'t be null'

    def test_correct_data(self):
        data = '{"path" : "/Users/zchornyi/Documents/apic-em-core/installers","instructions":[{"name":"java-parent",' \
               '"nodesForAdding": null,"artifactIdsForRemoving":["common-logging-api"],"nodesFroReplacing":[{' \
               '"groupId":"com.cisco.apicem","artifactId":"common-logging-core","version":"7.1.610","type":"jar",' \
               '"scope":"compile"}]}]} '
        response = Response.get_response(data, self.APPLICATION_URL)
        actual_result = json.loads(response.content)
        expected_result = Response.get_json_from_file(
            "files/responses/changeEndPoint/defaultCorrectResponse.json")
        assert expected_result == actual_result
