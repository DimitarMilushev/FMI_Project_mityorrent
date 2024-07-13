# Torrent Client
### Dependencies:
- Apache Commons CSV 1.10.0


###  Requirements:

- Support the following commands:


    register <user> <file1, file2, file3, …., fileN> - позволява на клиентите да „обявят“ кои файлове са налични за сваляне от тях. Чрез параметъра username, потребителят може да зададе свое уникално име (името, с което сървърът ще асоциира съответното IP).

    unregister <user> <file1, file2, file3, …., fileN> - потребителят обявява, че от него вече не могат да се свалят файловете <file1, file2, file3, …., fileN>.

    Забележка: За простота, не се интересуваме от security аспектите на решението, т.е. не е нужно да реализирате автентикация, която да гарантира, че даден потребител може да отрегистрира само файловете, които самият той е регистрирал.

    list-files – връща файловете, налични за изтегляне, и потребителите, от които могат да бъдат изтеглени.

    download <user> <path to file on user> <path to save> - изтегля дадения файл от съответния потребител.

- **Available files persistence**
  - Uses .csv for a db 
  - Updates users info every 30 seconds

| name | address |files... |
| ---- | ------- |:------:|
| xxxx | localhost:xxxx | PATH://to/file.exe, "D://text.txt" |

- **P2P** file transport
- Check users from the database and pull file using _download_ command
