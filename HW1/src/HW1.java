/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;

class Deck { // represents a pack of cards

    LinkedList<Integer> cards;
    // The methods toString, hashCode, equals, and copy are used for
    // display and testing, you should not modify them.

    @Override
    public String toString() {
        return cards.toString();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        Deck d = (Deck) o;
        return cards.equals(d.cards);
    }

    Deck copy() {
        Deck d = new Deck();
        for (Integer card : this.cards)
            d.cards.addLast(card);
        return d;
    }

    // constructor of an empty deck
    Deck() {
        cards = new LinkedList<Integer>();
    }

    // constructor from field
    Deck(LinkedList<Integer> cards) {
        this.cards = cards;
    }

    // constructor of a complete sorted deck of cards with nbVals values
    Deck(int nbVals) {
        cards = new LinkedList<Integer>();
        for (int j = 1; j <= nbVals; j++)
            for (int i = 0; i < 4; i++)
                cards.add(j);
    }

    // Question 1

    // takes a card from deck d to put it at the end of the current packet
    int pick(Deck d) {
        if (d.cards.isEmpty())
            return -1;
        int x = d.cards.removeFirst();
        cards.addLast(x);
        return x;
    }

    // takes all the cards from deck d to put them at the end of the current deck
    void pickAll(Deck d) {
        while (!d.cards.isEmpty())
            pick(d);
    }

    // checks if the current packet is valid
    boolean isValid(int nbVals) {
        int[] numbers = new int[nbVals]; // numbers[i] is the number of cards of value i+1
        for (Integer x : cards) {
            if (x < 1 || x > nbVals || numbers[x - 1] > 3)
                return false;
            numbers[x - 1]++;
        }
        return true;
    }

    // Question 2.1

    // chooses a position for the cut
    int cut() {
        int position = 0;
        for (int i = 0; i < cards.size(); i++)
            if (Math.random() < 0.5)
                position++;
        return position;
    }

    // cuts the current packet in two at the position given by cut()
    Deck split() {
        Deck d = new Deck();
        int position = cut();
        for (int i = 0; i < position; i++)
            d.pick(this);
        return d;
    }

    // Question 2.2

    // mixes the current deck and the deck d
    void riffleWith(Deck d) {
        Deck deck1 = this.copy();
        Deck deck2 = d.copy();
        cards.clear();
        while (!deck1.cards.isEmpty() && !deck2.cards.isEmpty()) {
            final double total = deck1.cards.size() + deck2.cards.size();
            if (Math.random() * total < deck1.cards.size()) // use * instead of / to avoid precision loss
                pick(deck1);
            else
                pick(deck2);
        }
        pickAll(deck1);
        pickAll(deck2);
    }

    // Question 2.3

    // shuffles the current deck using the riffle shuffle
    void riffleShuffle(int m) {
        for (int i = 0; i < m; i++) {
            Deck d = split();
            riffleWith(d);
        }
    }
}

class Battle { // represents a battle game

    Deck player1;
    Deck player2;
    Deck trick;

    // constructor of a battle without cards
    Battle() {
        player1 = new Deck();
        player2 = new Deck();
        trick = new Deck();
    }

    // constructor from fields
    Battle(Deck player1, Deck player2, Deck trick) {
        this.player1 = player1;
        this.player2 = player2;
        this.trick = trick;
    }

    // copy the battle
    Battle copy() {
        Battle r = new Battle();
        r.player1 = this.player1.copy();
        r.player2 = this.player2.copy();
        r.trick = this.trick.copy();
        return r;
    }

    // string representing the battle
    @Override
    public String toString() {
        return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli "
                + trick.toString();
    }

    // equality of battles
    @Override
    public boolean equals(Object o) {
        Battle b = (Battle) o;
        return player1.equals(b.player1) && player2.equals(b.player2) && trick.equals(b.trick);
    }

    // Question 3.1

    // constructor of a battle with a deck of cards of nbVals values
    Battle(int nbVals) {
        this.trick = new Deck(nbVals);
        this.trick.riffleShuffle(7);
        this.player1 = new Deck();
        this.player2 = new Deck();
        while (!this.trick.cards.isEmpty()) {
            this.player1.pick(this.trick);
            this.player2.pick(this.trick);
        }
    }

    // Question 3.2

    // test if the game is over
    boolean isOver() {
        return player1.cards.isEmpty() || player2.cards.isEmpty();
    }

    // performs one round of the game
    boolean oneRound() {
        if (isOver())
            return false;
        final int card1 = trick.pick(player1);
        final int card2 = trick.pick(player2);
        if (card1 == card2) {
            if (isOver())
                return false;
            trick.pick(player1);
            trick.pick(player2);
            return oneRound();
        }
        if (card1 > card2)
            player1.pickAll(trick);
        else
            player2.pickAll(trick);
        return true;
    }

    // Question 3.3

    // returns the winner
    int winner() {
        if (player1.cards.size() > player2.cards.size())
            return 1;
        if (player1.cards.size() < player2.cards.size())
            return 2;
        return 0;
    }

    // plays a game with a fixed maximum number of moves
    int game(int turns) {
        for (int i = 0; i < turns; i++)
            if (!oneRound())
                return winner();
        return winner();
    }

    // Question 4.1

    // plays a game without limit of moves, but with detection of infinite games
    int game() {
        Battle turtle = this.copy();
        Battle hare = this.copy();
        while (true) {
            if (!turtle.oneRound())
                return turtle.winner();
            if (!hare.oneRound() || !hare.oneRound())
                return hare.winner();
            if (turtle.equals(hare)) // I defined equality of Battle at line 169
                return 3;
        }
    }

    // Question 4.2

    // performs statistics on the number of infinite games
    static void stats(int nbVals, int nbGames) {
        int[] stats = new int[4];
        for (int i = 0; i < nbGames; i++) {
            Battle b = new Battle(nbVals);
            stats[b.game()]++;
        }
        System.out.println("For " + nbGames + " games with " + nbVals + " values, we have:");
        System.out.println(" - " + stats[0] + " draws");
        System.out.println(" - " + stats[1] + " player1 wins");
        System.out.println(" - " + stats[2] + " player2 wins");
        System.out.println(" - " + stats[3] + " infinite games");
    }
}
