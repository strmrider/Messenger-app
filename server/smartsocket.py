import socket

class SmartSocket:
    def __init__(self, socket_obj):
        self.socket = socket_obj

    def extractSize(self, crudeData):
        sizeInStr = ""
        isStarted = False
        for char in crudeData:
            if(char != '0'):
                isStarted = True
            if(char != '0' or (char == '0' and isStarted)):
                sizeInStr += char
        return int(sizeInStr)

    def recvAll(self, dataSize):
        data = self.socket.recv(dataSize)
        if data == "":
            return -1

        while (len(data) < dataSize):
            data += self.socket.recv(dataSize-(len(data)))

        return bytes(data)

    def recv(self):
        data_size = self.recvAll(5)

        if data_size == -1:
            return ""
        data_size = self.extractSize(data_size)
        data = self.recvAll(data_size)

        return data

    def get_size_in_format(self, data_size):
        str_size = str(data_size)
        completion = ""
        for i in xrange(5 - len(str_size)):
            completion += "0"
        return completion + str_size

    def send(self, data):
        data_size = self.get_size_in_format(len(data))
        self.socket.sendall(data_size + data)

    def disconnect(self):
        for i in xrange(3):
            self.send("OUT-123")
            approve = self.recv()
            if approve == "555":
                self.send("OUT-123")
                self.socket.close()
                return

        raise Exception("Error 555: Disconnection failure")


