import 'dart:ui';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:trial/auth.dart';
import 'package:trial/database_helper.dart';
import 'package:trial/conversation_detail.dart';
import 'package:trial/user.dart';
import 'package:trial/login_screen_presenter.dart';
import 'package:trial/home_screen.dart';
import 'package:trial/session.dart';
import 'package:trial/global.dart';
import 'package:trial/login_screen.dart';
import 'dart:convert';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'dart:async';


class CreateConversation extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new CreateConversationState();
  }
}

class CreateConversationState extends State<CreateConversation> {
  final TextEditingController _textController = new TextEditingController();

  void _handleChanged(String text){

  }

  Future helper(pattern) async{
    Session _session = new Session();
    GlobalStuff globStuff = new GlobalStuff();
    final Auto_URL = globStuff.getBase() + "/AutoCompleteUser";
   String s= await _session.post(Auto_URL,{"term":pattern});
      List<dynamic> returnStuff = json.decode(s);
      return returnStuff;
  }

  @override
  Widget build(BuildContext context) {

      return new Scaffold(
          appBar: new AppBar(title: const Text("Chats"),
              actions: <Widget>[
                IconButton(
                    icon: Icon(Icons.home),
                    onPressed: () {
                      Navigator.of(context).pushReplacement(new MaterialPageRoute(builder: (BuildContext context) => new HomeScreen()));
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
                      Session _session = new Session();
                      GlobalStuff globStuff = new GlobalStuff();
                      print(globStuff.getBase());
                      final LOGOUT_URL = globStuff.getBase() + "/LogoutServlet";
                      _session.get(LOGOUT_URL);
                      exit(0);
                    }
                )
              ]),
          body:
          new Center(
              child: new Column(
                  children: <Widget>[
                    new TypeAheadField(
                      textFieldConfiguration: TextFieldConfiguration(
                          autofocus: true,
                          style: DefaultTextStyle.of(context).style.copyWith(
                              fontStyle: FontStyle.italic
                          ),
                          decoration: InputDecoration(
                              border: OutlineInputBorder()
                          )
                      ),
                      suggestionsCallback: (pattern) async {


                        return await helper(pattern);
                      },
                      itemBuilder: (context, suggestion) {
                        return ListTile(
                          //leading: Icon(Icons.shopping_cart),
                         title: Text(suggestion['value']),
                          subtitle: Text(suggestion['label']),
                        );
                      },
                      onSuggestionSelected: (suggestion) {
                        Session _session = new Session();
                        GlobalStuff globStuff = new GlobalStuff();
                        final CreateConv_URL = globStuff.getBase() + "/CreateConversation";
                        _session.post(CreateConv_URL,{"other_id":suggestion['value']});
                        Navigator.of(context).pushReplacement(new MaterialPageRoute(builder: (BuildContext context) => new HomeScreen()));
                      },
                    )
                    //new
                  ]
              )
          )

      );

  }
}

