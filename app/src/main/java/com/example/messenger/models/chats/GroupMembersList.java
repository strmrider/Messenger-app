package com.example.messenger.models.chats;


import com.example.messenger.models.Contacts.Contact;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class GroupMembersList {
    private ArrayList<String> admins;
    private ArrayList<String> members;

    public GroupMembersList(ArrayList<String> admins, ArrayList<String> members)
    {
        this.admins = admins;
        this.members = members;
    }

    public GroupMembersList()
    {
        admins = new ArrayList<>();
        members = new ArrayList<>();
    }

    public GroupMembersList(String creatorAdmin, ArrayList<Contact> newMembers)
    {
        admins = new ArrayList<>();
        admins.add(creatorAdmin);
        members = new ArrayList<>();
        for(int i=0; i<newMembers.size(); i++)
            members.add(newMembers.get(i).getUsername());
    }

    public ArrayList<String> getList(boolean admins)
    {
        if (admins)
            return this.admins;
        else
            return this.members;
    }

    public GroupMembersList(String serializedData)
    {
        deserialize(serializedData);
    }

    boolean isMemberAdmin(String username)
    {
        for(int i=0; i<admins.size(); i++)
        {
            if(admins.get(i).equals(username))
                return true;
        }

        return false;
    }

    private org.w3c.dom.Document loadXMLFromString(String xml) throws Exception
    {
        admins = new ArrayList<>();
        members = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private void deserialize(String serializedData)
    {
        try {
            org.w3c.dom.Document xmlDoc = loadXMLFromString(serializedData);
            Node node = xmlDoc.getElementsByTagName("members").item(0);
            int numOfMembers = Integer.parseInt(node.getAttributes().item(0).getTextContent());
            boolean isAdmin;
            for(int i=0; i<numOfMembers; i++)
            {
                node = xmlDoc.getElementsByTagName("member").item(i);
                isAdmin = Boolean.valueOf(node.getAttributes().item(0).getTextContent());
                if(isAdmin)
                    admins.add(node.getTextContent());
                else
                    members.add(node.getTextContent());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String serializeMembers()
    {
        StringBuilder serialization = new StringBuilder("");
        String str;

        for(int i=0; i<admins.size(); i++)
        {
            str = "<member admin='true'>" + admins.get(i) + "</member>";
            serialization.append(str);
        }

        for(int i=0; i<members.size(); i++)
        {
            str = "<member admin='false'>" + members.get(i) + "</member>";
            serialization.append(str);
        }

        return serialization.toString();
    }

    public void add(String username, boolean admin)
    {
        if(admin)
            admins.add(username);
        else
            members.add(username);
    }

    public ArrayList<GroupMember> getMembersList()
    {
        ArrayList<GroupMember> groupMembers = new ArrayList<>();
        for(int i=0; i<admins.size(); i++)
            groupMembers.add(new GroupMember(admins.get(i), true));

        for(int i=0; i<members.size(); i++)
            groupMembers.add(new GroupMember(members.get(i), false));

        return groupMembers;
    }

    public ArrayList<String> getMembersList(boolean admins)
    {
        if(admins)
            return this.admins;
        else
            return members;
    }

    public ArrayList<String> getAllMembersArrayList()
    {
        ArrayList<String> allMembers = new ArrayList<>();
        allMembers.addAll(admins);
        allMembers.addAll(members);

        return allMembers;
    }

    public void addMembers(GroupMembersList groupMembersList)
    {
        admins.addAll(groupMembersList.getMembersList(true));
        members.addAll(groupMembersList.getMembersList(false));
    }

    void addMembers(ArrayList<Contact> newMembers, boolean asAdmin)
    {
        if(asAdmin)
        {
            for(int i=0; i<newMembers.size(); i++)
                admins.add(newMembers.get(i).getUsername());
        }
        else
        {
            for(int i=0; i<newMembers.size(); i++)
                members.add(newMembers.get(i).getUsername());
        }
    }

    public void addMembers(ArrayList<String> newMembers)
    {
        members.addAll(newMembers);
    }

    public void removeMembers(ArrayList<String> removedMembers)
    {
        admins.removeAll(removedMembers);
        members.removeAll(removedMembers);
    }

    public String serialize(boolean encapsulated)
    {
        if (encapsulated)
            return "<members size='" + (admins.size() + members.size()) +"'>" + serializeMembers() +
                "</members>";
        else
            return serializeMembers();

    }


    public String serializeForDB()
    {
        StringBuilder serialization = new StringBuilder("<members>");
        String str;
        serialization.append("<admins>");
        for(int i=0; i<admins.size(); i++)
        {
            str = "<admin>" + admins.get(i) + "</admin>";
            serialization.append(str);
        }

        for(int i=0; i<members.size(); i++)
        {
            str = "<member>" + members.get(i) + "</member>";
            serialization.append(str);
        }
        serialization.append("</members>");
        return serialization.toString();
    }

    public void eliminate(GroupMembersList groupMembersList)
    {
        ArrayList<String> removedAdmins = groupMembersList.getList(true);
        ArrayList<String> removedMembers = groupMembersList.getList(false);

        admins.removeAll(removedAdmins);
        members.removeAll(removedMembers);
    }

    public void appointAdmins(ArrayList<String> newAdmins)
    {
        members.removeAll(newAdmins);
        admins.addAll(newAdmins);
    }

    public void initSample()
    {
        admins.add("admin1");
        admins.add("admin2");
        members.add("member1");
        members.add("member2");
        members.add("member3");
        members.add("member4");
    }
}
