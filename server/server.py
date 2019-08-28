import socket
from thread import *
import models.reglist
import userhandler

users_list = models.reglist.RegisteredList()

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((socket.gethostname(), 6800))
server.listen(5)


while 1:
    print "listening..."
    connection, address = server.accept()
    print "accepted " + address[0]
    start_new_thread(userhandler.UserHandler, (connection,users_list,))

