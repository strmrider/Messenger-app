import xml.etree.ElementTree as Et

USERNAME = 0
STATUS = 1
STATUS_UPDATE = "50"


class StatusUpdate:
    def __init__(self, request):
        root = Et.fromstring(request)
        self.username = root[USERNAME].text
        self.status = root[STATUS].text

    def update_request(self):
        return "<request type='"+STATUS_UPDATE+"'><username>" + self.username + "</username>" \
                "<status>" + self.status + "</status></request>"
