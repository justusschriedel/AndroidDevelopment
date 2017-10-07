public class Pair<E, F> {
    private E first; private F second;

    public Pair(E e, F f) {
	first = e; second = f;
    }

    public E getFirst() {
	return this.first;
    }

    public F getSecond() {
	return this.second;
    }

    public void makeFirst(E e) {
	this.first = e;
    }

    public void makeSecond(F f) {
	this.second = f;
    }
}
