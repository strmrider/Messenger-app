import xml.etree.ElementTree as Et

USERNAME = 0

class ProfileImageUpdate:
    def __init__(self, request):
        self.original_request = request
        self.parse_media_request(self.extract_media_request(request))

    def parse_media_request(self, media_request):
        self.root = Et.fromstring(media_request)
        self.username = self.root[USERNAME]

    def extractSize(self, crudeData):
        sizeInStr = ""
        isStarted = False
        for char in crudeData:
            if (char != '0'):
                isStarted = True
            if (char != '0' or (char == '0' and isStarted)):
                sizeInStr += char
        return int(sizeInStr)

    def extract_media_request(self, request):
        size = self.extractSize(request[1] + request[2] + request[3] + request[4] + request[5])
        media_request = ""
        for i in range(0, size):
            media_request += request[i + 6]

        return media_request

    def request_to_client(self):
        return self.original_request

