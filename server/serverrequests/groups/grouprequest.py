import xml.etree.ElementTree as Et

GROUP_MEMBERS_REMOVAL = "42"
GROUP_ADMIN_APPOINTMENT = "45"

class GroupRequest:
    def __init__(self, request):
        self.root = Et.fromstring(request)
        self.sender = self.root[0].text
        self.username = self.root[2][0].text
        # subjected members could be removed, added or appointed to admin members
        self.subjected_members = self.parse_subjected_members(self.root)

    def parse_subjected_members(self, root):
        subjected_members = []
        size = int(root[1].attrib.get("size"))
        for i in range(size):
            subjected_members.append(root[1][i].text)

        return subjected_members

    def serialize_members(self, members_list, admin):
        xml = ""
        for member in members_list:
            if admin:
                xml += "<member admin='true'>"
            else:
                xml += "<member admin='false'>"
            xml += member + "</member>"
        return xml

    def serialize_full_members_list(self, admins, other_members):
        xml = "<members size='" + str(len(admins) + len(other_members)) + "'>"
        xml += self.serialize_members(admins, True)
        xml += self.serialize_members(other_members, False)
        xml += "</members>"
        return xml

    def serialize_partial_members_list(self, members, is_admin):
        xml = "<members size='" + str(len(members)) + "'>"
        xml += self.serialize_members(members, is_admin)
        xml += "</members>"
        return xml

    def serialize_subjected_members(self, is_admin):
        xml = "<members size='" + str(len(self.subjected_members)) + "'>"
        xml += self.serialize_members(self.subjected_members, is_admin)
        xml += "</members>"
        return xml