import 'dart:async';

import 'package:trial/session.dart';
import 'package:trial/user.dart';
import 'dart:convert';
import 'package:trial/global.dart';

class RestDatasource {
  Session _netUtil = new Session();
  GlobalStuff globStuff = new GlobalStuff();
  //static final _API_KEY = "somerandomkey";

  Future<String> login(String username, String password) {
    String LOGIN_URL = globStuff.getBase() + "/LoginServlet";
    return _netUtil.post(LOGIN_URL,  {
      //"token": _API_KEY,
      "userid": username,
      "password": password
    }).then((dynamic res) {
      //print(res.toString());
      //print(res.runtimeType.toString());
      //print(res["status"]);
      JsonDecoder mydecoder = JsonDecoder();
      res = mydecoder.convert(res);
      if(!res["status"])
      {
        print("Login was not successful");
        ///throw new Exception(res["message"]);
        return "The username/password is invalid";
      }
      return "Success";
    });
  }
}