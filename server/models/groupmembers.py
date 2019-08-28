

class GroupMembersList:
    def __init__(self, admins, other_members):
        self.admins = admins
        self.members = other_members

    def remove_members(self, members_to_remove):
        self.admins = [member for member in self.admins if member in members_to_remove]
        self.members = [member for member in self.other_members if member in members_to_remove]

    def add_members(self, members):
        self.members.extend(members)

    def add_members_by_single_list(self, members, admin = False):
        if admin:
            self.admins.extend(members)
        else:
            self.members.extend(members)

    def appoint_to_admin(self, members):
        self.members = [member for member in self.other_members if member in members_to_remove]
        self.add_members(members, True)