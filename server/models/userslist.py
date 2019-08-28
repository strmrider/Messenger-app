import user
from threading import Lock

locker = Lock()

class UsersList:
    def __init__(self):
        self.connected_users = {}
        self.disconnected_users = {}

        self.new_user("1111")
        self.new_user("2222")
        self.new_user("123456789")
        self.new_user("12456789")
        self.new_user("147236546")
        self.new_user("9877")

    def new_user(self, username):
        self.disconnected_users[username] = user.User(username)

    def add_user(self, user):
        locker.acquire()
        self.disconnected_users[username] = user
        locker.release()

    def new_group(self, group):
        locker.acquire()
        self.groups_list[group.username] = group
        locker.release()

    def user_connection(self, username, is_connected):
        locker.acquire()
        if is_connected:
            user = self.disconnected_users.pop(username)
            self.connected_users[user.username] = user
        else:
            user = self.connected_users.pop(username)
            self.disconnected_users[user.username] = user
        locker.release()

    def get_user(self, username):
        user = self.connected_users.get(username)
        if user is None:
            user = self.disconnected_users.get(username)

        return user

    def send_to_several_users(self, users, request):
        for username in users:
            user = self.get_user(username)
            if user is not None:
                user.send_request(request)