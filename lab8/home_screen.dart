import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:trial/auth.dart';
import 'package:trial/database_helper.dart';
import 'package:trial/user.dart';
import 'package:trial/login_screen_presenter.dart';
import 'package:trial/session.dart';
import 'package:trial/global.dart';

class HomeScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new HomeScreenState();
  }
}

class HomeScreenState extends State<HomeScreen> {
  bool _isLoading = true;
  String returnStuff = "";

  @override
  void initState()
  {
    Session _session = new Session();
    GlobalStuff globStuff = new GlobalStuff();
    //final BASE_URL = "http://192.168.1.103:8080/outlab6";
    print(globStuff.getBase());
    final HOME_URL = globStuff.getBase() + "/AllConversations";
    _session.get(HOME_URL).then((dynamic res)
    {
      returnStuff = res.toString();
      print(returnStuff);
      setState(() => _isLoading = false);
    }
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(title: const Text("Chats"),
      actions: <Widget>[
        IconButton(
          icon: Icon(Icons.home),
          onPressed: () {

          }
        ),

        IconButton(
            icon: Icon(Icons.create),
            onPressed: () {

            }
        ),

        IconButton(
            icon: Icon(Icons.exit_to_app),
            onPressed: () {

            }
        )
      ]),
          body: new Center(
        child: new Column(
        children: <Widget>[
          _isLoading ? new Text("Loading...") : new Text(returnStuff)
        ]
    )
    )
    );
    }
}