class Factorial1 {
    public static void main (String[] a) {
        System.out.println(new Fac1().ComputeFac(10));
    }
}

class Fac1 {

    public int ComputeFac (int num) {
        int num_aux;
        int i;
        int j;
        B c;
        A a;

        a = new A();
        i = a.get(1, true);
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num * (this.ComputeFac(num - 1));

        return num_aux;
    }
}

class Fac extends B {

    public int ComputeFac (int num) {
        int num_aux;
        int i;
        int j;
        B c;
        A a;

        a = new A();
        i = a.get(1, true);
        j = this.find(i);
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num * (this.ComputeFac(num - 1));

        return num_aux;
    }
}

class A extends B {
	int a1;
    public int got () {
        int i;
        int j;

        j = 1;
        i = j + 1;
        return i;
    }
}

class B extends C{
    int b1;
	int b2;
    public int get (int c, boolean b) {
        int i;
        int j;

        i = 1;
        j = i + 1;
        return i;
    }
}

class C {
    int c;
    public int find (int i) {
        if (i < 1) {
            i = 100;
        } else
            i = 0;
        return i;
    }
}