/*
 * Based on an answer by Richard Heap on stackoverflow.
 * Original link:
 * https://stackoverflow.com/questions/50299253/flutter-http-maintain-php-session
 */


import 'dart:async';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Session {
  static final Session _session = new Session._internal();
  factory Session(){
    return _session;
  }
  Session._internal();

  Map<String, String> headers = {};

  Future<String> get(String url) async {
    http.Response response = await http.get(url, headers: headers);
    updateCookie(response);
    return response.body;
  }

  Future<String> post(String url, dynamic data) async {
    http.Response response = await http.post(url, body: data, headers: headers);
    updateCookie(response);
    //print(response.runtimeType.toString());
    //print(response);
    //print(response.body.runtimeType.toString());
    JsonDecoder decoder = JsonDecoder();
    //print(decoder.convert(response.body).runtimeType.toString());
    //_InternalLinkedHashMap<String, dynamic> ans = decoder.convert(response.body);
    //print(decoder.convert(response.body).keys());
    //print(decoder.convert(response.body));
    //print(decoder.convert(response.body)["status"]);
    //print(decoder.convert(response.body)["status"].runtimeType.toString());
    return response.body;
  }

  void updateCookie(http.Response response) {
    String rawCookie = response.headers['set-cookie'];
    if (rawCookie != null) {
      int index = rawCookie.indexOf(';');
      headers['cookie'] =
      (index == -1) ? rawCookie : rawCookie.substring(0, index);
    }
  }
}