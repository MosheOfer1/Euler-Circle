public class line {
   private dot a;
   private dot b;

    public line(dot a, dot b) {
        this.a = a;
        this.b = b;
    }

    public boolean sameLine(line line){
        if (line.getA().getNumber()==this.a.getNumber() && line.getB().getNumber()==this.b.getNumber())
            return true;
        else return line.getA().getNumber() == this.b.getNumber() && line.getB().getNumber() == this.a.getNumber();
    }

    public dot getA() {
        return a;
    }

    public void setA(dot a) {
        this.a = a;
    }

    public dot getB() {
        return b;
    }

    public void setB(dot b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "line{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
