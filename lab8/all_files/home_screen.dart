import 'dart:ui';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:trial/auth.dart';
import 'package:trial/database_helper.dart';
import 'package:trial/conversation_detail.dart';
import 'package:trial/user.dart';
import 'package:trial/login_screen_presenter.dart';
import 'package:trial/session.dart';
import 'package:trial/global.dart';
import 'package:trial/login_screen.dart';
import 'dart:convert';


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
  final List<friend> _chat= [];
 List<friend> _chat_new= [];
  final TextEditingController _textController = new TextEditingController();

  void _handleChanged(String text){
    List<friend> _chat_temp= [];
    for(int i=0;i<_chat.length;i++)
      {
        if(_chat[i].uid.startsWith(text))
          {
            _chat_temp.add(_chat[i]);
          }
      }
    setState(() => _chat_new=_chat_temp);
  }


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
      Map<String, dynamic> returnStuff = json.decode(res.toString());
      print(res.toString());
      for (Map<String, dynamic> d in returnStuff['data']){
      _chat.add(new friend(uid : d['uid'],last_message : d['last_timestamp'],num_msgs: d['num_msgs']));
      _chat_new.add(new friend(uid : d['uid'],last_message : d['last_timestamp'],num_msgs: d['num_msgs']));
    }



      setState(() => _isLoading = false);
    }
    );
  }

  @override
  Widget build(BuildContext context) {
    if(_isLoading) {
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
                    new Text("Loading...")
                  ]
              )
          )

      );
    }
    else{
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
                    new TextField(
                      controller: _textController,
                      onChanged: _handleChanged,
                       decoration: new InputDecoration.collapsed(

                         border : new OutlineInputBorder(
                           borderRadius: new BorderRadius.circular(12.0),
                         ),
                           hintText: "Search conversation"),
              ),
                    new Flexible(                                             //new
                      child: new ListView.builder(                            //new
                      padding: new EdgeInsets.all(8.0),                     //new
                        //reverse: true, //new
                        itemBuilder: (context, index){
                          return ListTile(
                          title: Text(_chat_new[index].uid),
                            onTap : ()  {Navigator.of(context).pushReplacement(new MaterialPageRoute(builder: (BuildContext context) => new ConvoDetail(uid:_chat_new[index].uid),
                          )
                          );
                            },
                          );

                          },      //new
                        itemCount: _chat_new.length,                          //new
                      ),                                                      //new
                    ),                                                        //new
                  ]
              )
          )

      );
    }
    }
}

class friend extends StatelessWidget{
  final String uid;
  final String last_message;
  final int num_msgs;
  friend({this.uid,this.last_message,this.num_msgs});
  @override
  Widget build(BuildContext context) {
    return new Container(
      margin: const EdgeInsets.symmetric(vertical: 10.0),
      child: new Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          new Container(
            margin: const EdgeInsets.only(right: 16.0),
            child: new CircleAvatar(child: new Text(uid[0])),
          ),
          new Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              new Text(uid, style: Theme.of(context).textTheme.subhead),
              new Container(
                margin: const EdgeInsets.only(top: 5.0),
                child: last_message == null ? new Text("") : new Text(last_message),
              ),
            ],
          ),
        ],
      ),
    );
  }


}