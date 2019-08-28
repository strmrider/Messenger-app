from threading import Lock
import Queue
import usercontacts

param_lock = Lock()

class User:
    def __init__(self, username):
        self.group = False
        self.connected = True
        self.paused = False
        self.username = username
        self.connection = None
        self.status = "Default status"
        self.contacts = usercontacts.UserContacts()
        self.pending_requests = Queue.Queue()

    def send_request(self, request):
        if self.connection and self.paused is False:
            self.connection.send(request)
        else:
            self.pending_requests.put(request)

    def connect(self, connection):
        param_lock.acquire()
        self.connection = connection
        self.connected = True
        param_lock.release()

    def disconnect(self):
        param_lock.acquire()
        self.connection = None
        self.connected = False
        param_lock.release()

    def hold(self):
        param_lock.acquire()
        self.paused = True
        param_lock.release()

    def send_pending_requests_list(self):
        if self.connected:
            param_lock.acquire()
            while not self.pending_requests.empty():
                self.connection.send(self.pending_requests.get())
            param_lock.release()

    def set_follower(self, follower):
        self.contacts.set_follower(follower)

    def remove_follower(self, follower):
        self.contacts.remove_follower(follower)

    def send_approved_contacts(self, users_list):
        self.send_request(self.contacts.get_registered_contacts_request(users_list))

    def update_contacts(self, contact_update, users_list):
        return self.contacts.apply_update(contact_update, users_list, self.username)