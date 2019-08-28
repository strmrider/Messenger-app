import xml.etree.ElementTree as Et

MESSAGE_TEXT = 0
MESSAGE = 0
ID = 0
SENDER = 1
RECIPIENT = 2
SENT_DATE = 3
BODY = 4

CHAT = 1

SENT_STATUS = "12"

class NewMessageRequest:
    def __init__(self, request):
        root = Et.fromstring(request)
        self.id = root[MESSAGE][ID].text
        self.sender = root[MESSAGE][SENDER].text
        self.recipient = root[MESSAGE][RECIPIENT].text
        self.sending_date = root[MESSAGE][SENT_DATE].text
        self.message_text = root[MESSAGE][BODY][MESSAGE_TEXT].text
        if self.message_text is None:
            self.message_text = ""

    def sent_approval(self):
        return "<request type='" + SENT_STATUS + "'>" + \
                "<id>"+self.id+"</id>" + \
                "<status>" + SENT_STATUS + "</status>" + \
                "<chat>" + self.recipient + "</chat>" + \
                "</request>"

    def __repr__(self):
        return "new message \nfrom: " + self.sender + "\nto: " + self.recipient + "\non: " + self.sending_date + \
                "\nid: " + self.id + "\ntext: " + self.message_text