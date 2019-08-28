import smartsocket
import serverrequests.requesthandler

class UserHandler:
    def __init__(self, socket, users_list):
        self.user = None # User object
        self.users_list = users_list
        self.requests_handler = None
        # run thread
        self.handle_client(socket)

    def handle_client(self, conn):
        client = smartsocket.SmartSocket(conn)
        username = client.recv()
        print username + " has connected"
        user = self.users_list.get_user(username)
        if user:
            user.connect(client)
            self.user = user
            self.user.send_pending_requests_list()
        else:
            print("User isn't registered to the system")

        self.requests_handler = serverrequests.requesthandler.RequestHandler(self.user, self.users_list)

        while True:
            data = client.recv()
            if data != "":
                print "received from " + username +": " + data
                self.requests_handler.handle_request(data)
            elif data == "OUT-113":
                self.user.disconnect()
                print username + " has disconnected"
            else:
                self.user.hold()
                print username + " has paused"
                break