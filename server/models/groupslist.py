import user
from threading import Lock

locker = Lock()

class GroupsList:
    def __init__(self):
        self.groups = {}

    def add_group(self, group):
        self.groups[group.username] = group

    def remove_group(self, group_username):
        self.groups.get(group_username).pop()

    def get_group(self, group_username):
        return self.groups.get(group_username)