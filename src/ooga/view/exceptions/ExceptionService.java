package ooga.view.exceptions;

public interface ExceptionService {
  void handleFatalError(Exception subProcessFatalError);
  void handleWarning(Exception warning);
}
