import xml.etree.ElementTree as Et
import models.groupmembers
import grouprequest

ADMINS_LIST = 0
OTHER_MEMBERS = 1
GROUP_INVITATION = "41"
GROUP_ADDITION = "44"

class GroupInvitationRequest(grouprequest.GroupRequest):
    def __init__(self, request):
        grouprequest.GroupRequest.__init__(self, request)
        self.display_name = self.root[2][1].text
        #self.current_members = self.parse_current_members(self.root)

    def parse_current_members(self, root):
        current_members = [[],[]]
        size = int(root[2][2].attrib.get("size"))
        for i in range(size):
            admin = root[2][2][i].attrib.get("admin")
            if admin == "true":
                current_members[ADMINS_LIST].append(root[2][2][i].text)
            else:
                current_members[OTHER_MEMBERS].append(root[2][2][i].text)

        return current_members


    def serialize_all_members(self):
        size = len(self.current_members[ADMINS_LIST]) + len(self.current_members[OTHER_MEMBERS]) + \
               len(self.subjected_members)
        xml = "<members size='" + str(size) + "'>"
        xml += self.serialize_members(self.current_members[ADMINS_LIST], True)
        xml += self.serialize_members(self.current_members[OTHER_MEMBERS], False)
        xml += self.serialize_members(self.subjected_members, False)
        xml += "</members>"

        return xml

    def make_members_list(self):
        return groupmembers.GroupMembersList(self.current_members[0], self.current_members[1])

    def invite_request_to_client(self, members_list, partial=False):
        if partial:
            members = self.serialize_partial_members_list(members_list, False)
        else:
            members = self.serialize_full_members_list(members_list[0], members_list[1])

        return "<request type='" + GROUP_INVITATION + "'>" + "<group><username>" + self.username + "</username>" + \
                "<display>" + self.display_name + "</display>" + members + "</group></request>"

    def make_update_request(self, current_members):
        return "<request type='" + GROUP_ADDITION + "'>" + "<group><username>"+self.username+"</username>" + \
                self.serialize_partial_members_list(self.subjected_members, False) + "</group></request>"


    def __repr__(self):
        return "group: " + self.username + "\ncalled: " + self.display_name + "\ninvited: " + self.subjected_members.__str__() +\
               "\ncurrent members" + self.current_members.__str__()
