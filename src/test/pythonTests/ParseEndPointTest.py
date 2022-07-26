import json

from ResponseFunctions import Response
from ResponseFunctions import JsonResponse


class TestParse:
    APPLICATION_URL = "http://localhost:8080/project/create"

    def test_data_without_path(self):
        data = '{"path" : null}'
        response = Response.get_response(data, self.APPLICATION_URL)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Path can\'t be null'

    def test_instruction_without_name(self):
        data = '{"path" : "main.py", "instructions" : [{"name":null}]}'
        response = Response.get_response(data, self.APPLICATION_URL)
        json_content = json.loads(response.content)
        json_response = JsonResponse(**json_content[0])
        assert json_response.httpStatus == 400
        assert json_response.message == 'Name can\'t be null'

    def test_correct_data(self):
        data = '{"path" : "/Users/zchornyi/IdeaProjects/withAll/src/test/pythonTests/files/poms/pom.xml"}'
        response = Response.get_response(data, self.APPLICATION_URL)
        actual_result = json.loads(response.content)
        expected_result = Response.get_json_from_file(
            "files/responses/parseEndPoint/defaultResult.json")
        assert expected_result == actual_result

    def test_data_with_new_project(self):
        data = '{"path":"/Users/zchornyi/IdeaProjects/withAll/src/test/pythonTests/files/poms/pom' \
               '.xml","instructions":[{"name":"Parse","nodesForAdding": [{"groupId":"org.agiso.core",' \
               '"artifactId":"agiso-core-i18n","version":"0.0.2.RELEASE","type":"jar","scope":"compile"}],' \
               '"artifactIdsForRemoving":["aether-api", "aether-spi", "aether-util"],"nodesFroReplacing":[{' \
               '"groupId":"org.jboss.shrinkwrap.resolver","artifactId":"shrinkwrap-resolver-impl-maven",' \
               '"version":"3.1.4","type":"jar","scope":"test"}]}]} '
        response = Response.get_response(data, self.APPLICATION_URL)
        actual_result = json.loads(response.content)
        expected_result = Response.get_json_from_file(
            "files/responses/parseEndPoint/resultWithNewProject.json")
        assert expected_result == actual_result

