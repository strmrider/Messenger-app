import xml.etree.ElementTree as Et

class ContactsRequest:
    def __init__(self, request):
        self.contacts = []
        self.parse_contacts(request)

    def parse_contacts(self, request):
        root = Et.fromstring(request)
        size = int(str(root[1].attrib.get("size")))
        for i in range(size):
            self.contacts.append(root[1][i].text)
