import xml.etree.ElementTree as Et
CONTACTS_APPROVAL = "70"

class ContactsUpdateRequest:
    def __init__(self, request):
        self.contact = None
        self.former_username = None
        self.operation = None
        self.parse_contacts(request)

    def parse_contacts(self, request):
        root = Et.fromstring(request)
        self.operation = int(root[0].text)
        self.contact = root[1].text
        if self.operation == 3:
            self.former_username =  root[2].text

    def new_contact_registration_approval(self):
        return "<request type='" + CONTACTS_APPROVAL + "'><contacts size='1'><contact>" + self.contact + "</contact>" \
                                                                                                "</contacts></request>"
