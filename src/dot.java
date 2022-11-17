public class dot {
    private int x;
    private int y;
    private int number;
    private int[] pointers = new int[0];

    public dot(int x, int y) {
        this.x = x;
        this.y = y;
        this.number = -1;
    }
    public dot(int x, int y,int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }
    public dot(dot dot){
        this.x = dot.getX();
        this.y = dot.getY();
        this.number = dot.getNumber();
        this.pointers = dot.getPointers();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getPointers() {
        return pointers;
    }
    public void addPointer(int pointer) {
        int[] newPointers = new int[pointers.length+1];
        System.arraycopy(pointers, 0, newPointers, 0, pointers.length);
        newPointers[pointers.length] = pointer;
        this.pointers = newPointers;
    }
    public void setPointers(int[] pointers) {
        this.pointers = pointers;
    }
    public void removePointer(int pointer){
        for (int i = 0; i < pointers.length; i++) {
            if (pointers[i]==pointer){
                int[] newPointers = new int[pointers.length-1];
                System.arraycopy(pointers, 0, newPointers, 0, i);
                if (pointers.length - (i + 1) >= 0)
                    System.arraycopy(pointers, i + 1, newPointers, i + 1 - 1, pointers.length - (i + 1));
                pointers = newPointers;
                return;
            }
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public boolean equal(dot a){
        return this.getX() + (3*Euler.SIZE) >= a.getX() && this.getX() - (3*Euler.SIZE) <= a.getX()
                && this.getY() + (3*Euler.SIZE) >= a.getY() && this.getY() - (3*Euler.SIZE) <= a.getY();
    }

    @Override
    public String toString() {
        return "dot{" +
                "number=" + number +
                '}';
    }
}
