package EmBoxUnity.Commands.Core;

public interface ICommand{
int GUID = 0;

void ExecuteIn();

void ExecuteOut();

void Toggle();

void AddOnInComplete( IAction act, int guid );

void RemOnInComplete( int guid );

void AddOnOutComplete( IAction act, int guid );

void RemOnOutComplete( int guid );

void update();

StatesC State = StatesC.ExecuteNone;

}

