package ooga.view.exceptions;

public interface ExceptionService {
  void handleFatalError(UIServicedException subProcessFatalError);
  void handleWarning(UIServicedException warning);
}
