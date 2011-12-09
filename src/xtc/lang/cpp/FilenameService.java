/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2009-2011 New York University
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package xtc.lang.cpp;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.DataOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server that emits lines of a text file one at a time and a client
 * that receives them.  Several clients can access the server and each
 * will get a unique line of the file.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.4 $
 */
class FilenameService {

  public static void main(String args[]) {
    if (args.length < 3) {
      System.err.println(
                         "USAGE: \n" +
                         "  java FilenameService -server port file\n" +
                         "  java FilenameService -client server port\n" +
                         "\n" +
                         "EXIT CODES:\n" +
                         "  3 - no more files available\n" +
                         "  4 - file server not reachable\n" +
                         "  1 - other errors\n" +
                         ""
                         );
      System.exit(0);
    }

    String mode = args[0];

    if (mode.equals("-server")) {
      try {
        int port = Integer.parseInt(args[1]);
        String filelist = args[2];

        BufferedReader br = new BufferedReader(new FileReader(filelist));
        String filename = br.readLine();

        ServerSocket server = new ServerSocket(port);

        while (null != filename) {
          Socket socket = server.accept();
          DataOutputStream out = new DataOutputStream(socket.getOutputStream());

          System.out.println(filename);

          out.writeBytes(filename);

          socket.close();

          filename = br.readLine();
        }

        // Once the files are done, the server writes out "$end" to show
        // that it's done.

        while (true) {
          Socket socket = server.accept();
          DataOutputStream out = new DataOutputStream(socket.getOutputStream());

          out.writeBytes("$end");

          socket.close();
        }

      } catch (java.io.IOException e) {
        e.printStackTrace();
        System.exit(1);
      }

    } else if (mode.equals("-client")) {
      try {
        String server = args[1];
        int port = Integer.parseInt(args[2]);
        Socket socket= new Socket(server, port);
        BufferedReader in
          = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String filename = in.readLine();

        if (filename.equals("$end")) {
          socket.close();
          System.exit(3);
        }

        System.out.println(filename);

        socket.close();

      } catch (java.net.ConnectException e) {
        e.printStackTrace();
        System.exit(4);

      } catch (java.io.IOException e) {
        e.printStackTrace();
        System.exit(1);
      }

    } else {
      System.err.println("invalid flag");
    }
  }
}