
class Group:
    def __init__(self, username, display_name, group_members):
        self.group = True
        self.username = username
        self.display_name = display_name
        self.group_members = group_members

    def remove_members(self, members):
        self.group_members.remove_members(members)

    def add_members(self, members):
        self.group_members.add_members(members)

    def appoint_admins(self, admins):
        self.appoint_to_admin(admins)

    def send_request_per_list(self, request, sender, user_list, members_list):
            for member in members_list:
                user = user_list.get_user(member)
                if user is not None and user.username != sender:
                    user.send_request(request)

    def send_request_to_group(self, request, sender, users_list):
        self.send_request_per_list(request, sender, users_list, self.group_members.admins)
        self.send_request_per_list(request, sender, users_list, self.group_members.members)

