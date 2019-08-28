import xml.etree.ElementTree as Et
import grouprequest

class AdminAppointment(grouprequest.GroupRequest):
    def __init__(self, request):
        grouprequest.GroupRequest.__init__(self, request)

    def update_request(self):
        return "<request type='" + grouprequest.GROUP_ADMIN_APPOINTMENT + "'>" + "<group><username>" + self.username +\
               "</username>" + self.serialize_subjected_members(True) + "</group></request>"