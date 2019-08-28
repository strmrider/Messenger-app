import xml.etree.ElementTree as Et

ID = 0
SENDER = 1
RECIPIENT = 2
MESSAGE_RECEIVED = "13"

class StatusChangeRequest:
    def __init__(self, request):
        root = Et.fromstring(request)
        self.id = root[ID].text
        self.sender = root[SENDER].text
        self.recipient = root[RECIPIENT].text

    def to_request_to_user(self):
        return "<request type='" + MESSAGE_RECEIVED + "'>" + \
                "<id>" + self.id + "</id>" + \
                "<status>" + MESSAGE_RECEIVED + "</status>" + \
                "<chat>" + self.sender + "</chat>" + \
                "</request>"

    def __repr__(self):
        return "message received \nfrom: " + self.sender + "\nto: " + self.recipient + "\nis: " + self.id