package EmBoxUnity.Commands.Core;

public interface ICommandComposition extends ICommand{
	void Add( ICommand cmnd );
}

