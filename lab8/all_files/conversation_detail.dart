import 'dart:ui';
import 'dart:io';
import 'package:trial/login_screen.dart';
import 'package:flutter/material.dart';
import 'package:trial/auth.dart';
import 'package:trial/database_helper.dart';
import 'package:trial/user.dart';
import 'package:trial/login_screen_presenter.dart';
import 'package:trial/session.dart';
import 'package:trial/global.dart';
import 'package:trial/home_screen.dart';
import 'dart:convert';

class ConvoDetail extends StatefulWidget {
 final String uid;
 ConvoDetail({Key key, @required this.uid}) : super(key:key);
  //{
    //uid = u;
  //}

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new ConvoDetailState();
  }
}



class ConvoDetailState extends State<ConvoDetail> {
  bool _isLoading = true;
  String returnStuff = "";
  final List<friend> _chat= [];
  final TextEditingController _textController = new TextEditingController();

  void _handleSubmitted(String text) {
    Session _session = new Session();
    GlobalStuff globStuff = new GlobalStuff();
    final NEWCONVO_URL = globStuff.getBase() + "/NewMessage";
    _session.post(NEWCONVO_URL, {"other_id":widget.uid, "msg":text}).then((dynamic res)
    {
      Map<String, dynamic> returnStuff = json.decode(res.toString());
      print(res.toString());
      if (! returnStuff["status"]) {
        print("There was an error in adding the message");
      }
        Navigator.of(context).pushReplacement(new MaterialPageRoute(builder: (BuildContext context) => new ConvoDetail(uid:widget.uid),
        )
        );
    }
    );
        _textController.clear();
  }

  @override
  void initState()
  {
    Session _session = new Session();
    GlobalStuff globStuff = new GlobalStuff();
    //final BASE_URL = "http://192.168.1.103:8080/outlab6";
    //print(globStuff.getBase());
    final CONVODETAIL_URL = globStuff.getBase() + "/ConversationDetail";
    _session.post(CONVODETAIL_URL, {"other_id":widget.uid}).then((dynamic res)
    {
      Map<String, dynamic> returnStuff = json.decode(res.toString());
      print(res.toString());
      for (Map<String, dynamic> d in returnStuff['data']){
        _chat.add(new friend(timestamp : d['timestamp'],text : d['text'],uid: d['uid']));
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
                    new Flexible(                                             //new
                      //expanded: true,//new
                      child: new ListView.builder(                            //new
                        padding: new EdgeInsets.all(8.0),
                        reverse: true,                                        //new
                        itemBuilder: (_, int index) => _chat[_chat.length-index-1],      //new
                        itemCount: _chat.length,                          //new
                      ),                                                      //new
                    ),
                    new Row(
                        children: <Widget>[
                          new Flexible(
                            child: new TextField(
                            controller: _textController,
                            onSubmitted: _handleSubmitted,
                            decoration: new InputDecoration.collapsed(
                            hintText: "Send a message"),
                            ),
                          ),
                          new Container(                                                 //new
                            margin: new EdgeInsets.symmetric(horizontal: 4.0),           //new
                            child: new IconButton(                                       //new
                            icon: new Icon(Icons.send),                                //new
                            onPressed: () => _handleSubmitted(_textController.text)),  //new
                          ),                                                             //new
                        ],
                    ),
                  ]

              )
          )

      );
    }
  }
}

class friend extends StatelessWidget{
  final String timestamp;
  final String text;
  final String uid;
  friend({this.timestamp,this.text,this.uid});
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
                child:  Text(text + "\n"+ timestamp),
              ),
            ],
          ),
        ],
      ),
    );
  }


}