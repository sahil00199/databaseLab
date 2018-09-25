import 'package:trial/rest_ds.dart';
import 'package:trial/user.dart';

abstract class LoginScreenContract {
  void onLoginSuccess();
  void onLoginError(String errorTxt);
}

class LoginScreenPresenter {
  LoginScreenContract _view;
  RestDatasource api = new RestDatasource();
  LoginScreenPresenter(this._view);

  doLogin(String username, String password) {
    api.login(username, password).then((String returnStatus) {
      if (returnStatus == "Success") {
        _view.onLoginSuccess();
      }
      else{
        _view.onLoginError(returnStatus);
      }
  });
}
}