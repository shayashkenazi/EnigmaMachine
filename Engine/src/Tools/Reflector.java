package Tools;

public class Reflector extends Switcher {

    public Reflector (String ID,int lengthABC) {
        super(ID,lengthABC);
    }

    @Override
    public int Switch(int input, boolean direction) {
        return inputOutput.get(input).getValue();
    }

}
