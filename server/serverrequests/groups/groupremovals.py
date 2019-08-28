import xml.etree.ElementTree as Et
import grouprequest

class GroupMembersRemoval(grouprequest.GroupRequest):
    def __init__(self, request):
        grouprequest.GroupRequest.__init__(self, request)

    def update_request(self):
        return "<request type='"+ grouprequest.GROUP_MEMBERS_REMOVAL +"'>" + "<group><username>" + self.username +\
               "</username>" + self.serialize_subjected_members(False) + "</group></request>"