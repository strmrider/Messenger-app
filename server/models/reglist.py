import userslist
import groupslist

class RegisteredList:
    def __init__(self):
        self.users_list = userslist.UsersList()
        self.groups_list = groupslist.GroupsList()

    def get_user(self, username):
        user = self.groups_list.get_group(username)
        if user is None:
            user = self.users_list.get_user(username)
        return user