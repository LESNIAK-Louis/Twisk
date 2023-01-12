GCC=gcc
CLIENT=client
LIB=libTwisk.so

ARGS_CLIENT = -Wall -fPIC -c 
ARGS_LIB = -shared programmeC.o $(CLIENT).o -o $(LIB)
ARGS_MAIN= -Wall -L. main.c -o main -lTwisk

all:
ifeq ($(wildcard $(CLIENT).o),)
	$(GCC) $(ARGS_CLIENT) $(CLIENT).c
endif
ifeq ($(wildcard $(LIB)),)
	$(GCC) $(ARGS_LIB)
endif
	$(GCC) $(ARGS_MAIN)

clean: 
	rm -f $(CLIENT).o $(LIB) main