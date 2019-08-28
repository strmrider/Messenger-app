import messages.newmessage
import messages.mediamessage
import messages.statusupdate

import groups.groupinvitation
import groups.groupremovals
import groups.adminappointment

import models.group
import models.groupmembers

import profiles.contacts
import profiles.contactupdate
import profiles.status
import profiles as Profiles
import xml.etree.ElementTree as Et

NONE = 0
NEW_MEDIA_MESSAGE = 10
NEW_MESSAGE = 11
MESSAGE_SENT_APPROVAL = 12
MESSAGE_RECEIVED_APPROVAL = 13
MESSAGE_READ_APPROVAL = 14
GROUP_INVITATION = 41
GROUP_MEMBER_REMOVAL = 42
GROUP_REMOVAL_NOTE = 43
GROUP_MEMBER_ADDITION = 44
GROUP_ADMIN_APPOINTMENT = 45
PROFILE_IMAGE_UPDATE = 50
STATUS_UPDATE = 51
CONTACTS_LIST_UPDATE = 60
CONTACTS_APPROVAL = 70

class RequestHandler:
    def __init__(self, user, users_list):
        self.user = user
        self.registered_list = users_list

    def get_request_type(self, request):
        if request[0] == "M":
            return NEW_MEDIA_MESSAGE
        else:
            root = Et.fromstring(request)
            return int(root.attrib.get("type"))

    def send_to_target(self, target, request, sender):
        if target.group:
            target.send_request_to_group(request, sender, self.registered_list)
        else:
            target.send_request(request)

    def new_message(self, type, request):
        if type == NEW_MEDIA_MESSAGE:
            new_message = messages.mediamessage.NewMediaMessage(request)
        else:
            new_message = messages.newmessage.NewMessageRequest(request)

        self.user.send_request(new_message.sent_approval())
        target = self.registered_list.get_user(new_message.recipient)
        self.send_to_target(target, request, new_message.sender)

    def message_status_change(self, request):
        status_change = messages.statusupdate.StatusChangeRequest(request)
        target = self.registered_list.get_user(status_change.recipient)
        self.send_to_target(target, status_change.to_request_to_user(), status_change.sender)

    def group_invitation(self, request):
        group_invitation = groups.groupinvitation.GroupInvitationRequest(request)
        group = self.registered_list.get_user(group_invitation.username)
        # group doesn't exist
        if group is None:
            # creates new members list and add the invited
            members_list = [[group_invitation.sender], group_invitation.subjected_members]
            group_members = models.groupmembers.GroupMembersList([group_invitation.sender], group_invitation.subjected_members)
            # creates new group
            group = models.group.Group(group_invitation.username, group_invitation.display_name, group_members)
            self.registered_list.groups_list.add_group(group)
            # send invitation to new members
            group.send_request_to_group(group_invitation.invite_request_to_client(members_list), group_invitation.sender,
                                        self.registered_list)

        else:
            all_members = [group.group_members.admins, group.group_members.members + group_invitation.subjected_members]
            self.registered_list.users_list.send_to_several_users(group_invitation.subjected_members,
                                                                  group_invitation.invite_request_to_client(all_members))
            group.send_request_to_group(group_invitation.make_update_request(all_members), group_invitation.sender, self.registered_list)
            group.add_members(group_invitation.subjected_members)

    def group_member_removal(self, request):
        group_removal = groups.groupremovals.GroupMembersRemoval(request)
        group = self.registered_list.get_user(group_removal.username)
        group.send_request_to_group(group_removal.update_request(), group_removal.sender, self.registered_list)

    def group_admin_appointment(self, request):
        group_appoint = groups.adminappointment.AdminAppointment(request)
        group = self.registered_list.get_user(group_appoint.username)
        group.send_request_to_group(group_appoint.update_request(), group_appoint.sender, self.registered_list)

    def status_update(self, request):
        status_update = profiles.status.StatusUpdate(request)
        user = self.registered_list.get_user(status_update.username)
        user.send_request(status_update.update_request())

    def approve_contacts(self, request):
        contacts_list = profiles.contacts.ContactsRequest(request)
        self.user.contacts.sort(contacts_list.contacts, self.registered_list, self.user.username)
        self.user.send_approved_contacts(self.registered_list)

    def update_contacts_list(self, request):
        updated_contact = profiles.contactupdate.ContactsUpdateRequest(request)
        result = self.user.update_contacts(updated_contact, self.registered_list)
        # contact addition- contact is registered
        if result:
            self.user.send_request(updated_contact.new_contact_registration_approval())

    def handle_request(self, request):
        type = self.get_request_type(request)

        if type == NEW_MESSAGE or type == NEW_MEDIA_MESSAGE:
            self.new_message(type, request)

        elif MESSAGE_READ_APPROVAL >= type >= MESSAGE_SENT_APPROVAL:
            self.message_status_change(request)

        elif type == GROUP_INVITATION:
            self.group_invitation(request)

        elif type == GROUP_MEMBER_REMOVAL:
            self.group_member_removal(request)

        elif type == GROUP_ADMIN_APPOINTMENT:
            self.group_admin_appointment(request)

        elif type == STATUS_UPDATE:
            self.status_update(request)

        elif type == CONTACTS_APPROVAL:
            self.approve_contacts(request)

        elif type == CONTACTS_LIST_UPDATE:
            self.update_contacts_list(request)



