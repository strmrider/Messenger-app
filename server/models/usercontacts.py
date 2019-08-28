CONTACTS_APPROVAL = "70"
CONTACT_ADDED = 1
CONTACT_REMOVED = 2
CONTACTS_REPLACED = 3

class UserContacts:
    def  __init__(self):
        self.registered = []
        self.unregistered = []
        self.followers = []

    def sort(self, contacts, users_list, username):
        for contact in contacts:
            contact_obj = users_list.get_user(contact)
            if contact_obj is not None:
                self.registered.append(contact)
                contact_obj.set_follower(username)
            else:
                self.unregistered.append(contact)

    def set_follower(self, follower):
        self.followers.append(follower)

    def remove_follower(self, follower):
        self.followers.remove(follower)

    def serialize_registered(self, users_list):
        xml = "<contacts size='" + str(len(self.registered))+ "'>"
        for contact in self.registered:
            xml += "<contact>"
            xml += ("<username>" + contact + "</username>")
            user = users_list.get_user(contact)
            xml += ( "<status>" + user.status + "</status>" )
            xml += "</contact>"

        xml += "</contacts>"

        return xml

    def get_registered_contacts_request(self, users_list):
        return "<request type='" + CONTACTS_APPROVAL + "'>" + self.serialize_registered(users_list) + "</request>"

    def new_contact_update(self, new_contact, users_list, username):
        user = users_list.get_user(new_contact)
        if user is not None:
            self.registered.append(new_contact)
            user.set_follower(username)
            return True
        else:
            self.unregistered.append(new_contact)

    def remove_contact_update(self, removed_contact, users_list, username):
        if removed_contact in self.registered:
            self.registered.remove(removed_contact)
        else:
            self.unregistered.remove(removed_contact)
        user = users_list.get_user(removed_contact)
        if user is not None:
            user.remove_follower(username)

    def apply_update(self, contacts_update, users_list, username):
        if contacts_update.operation == CONTACT_ADDED:
            return self.new_contact_update(contacts_update.contact, users_list, username)

        elif contacts_update.operation == CONTACT_REMOVED:
            self.remove_contact_update(contacts_update.contact, users_list, username)

        elif contacts_update.operation == CONTACTS_REPLACED:
            self.remove_contact_update(contacts_update.former_username, users_list, username)
            self.new_contact_update(contacts_update.contact, users_list, username)




