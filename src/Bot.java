import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Bot {
    private char figure;
    private char opponent;
    private int gameMove;

    public Bot(char fig) {
        figure = fig;
        if (figure == 'X') opponent = '0';
        else opponent = 'X';
        gameMove = 0;
    }

    public char[][] getOpinion(char[][] stat) {
        ArrayList<int[]> ratioOfVar = new ArrayList<int[]>();

        //формируем возможные варианты хода для бота
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (stat[i][j] != figure && stat[i][j] != opponent) {
                    ratioOfVar.add(checkWinLines(i,j,getLineRowAndDiagonals(stat)));
                }
            }
        }
        int[] max = Collections.max(ratioOfVar, new CompareArray());
        stat[max[1]][max[2]] = figure;
        return stat;
    }



    public int[] checkWinLines(int i, int j, ArrayList<ArrayList<Character>> s) {
        //первое число возвращаемого массива - оценка выигрышных линий на позиции и кол-во фигур в них,
        //остальные два стобец и строка (i,j).
        //оценка: если на линии стоят три фигуры бота, то возвращаем 100.
        //если есть необходимость перекрыть ход, то вернем 50
        //иначе считаем сумму выигрышных линий, суммируем кол-во фигур в них и возвращаем

        int sumRat = 0;

        //проверить столбцы
        int countFig = 0;
        int countOpp = 0;
        s.get(i).set(j,figure);
        for (Character a: s.get(i))
            if (a.equals(figure)) countFig++;
            else if(a.equals(opponent)) countOpp++;
        if (countOpp == 0) {
            if (countFig == 3) return new int[]{100, i, j};
            else if (countFig == 2) sumRat += 2;
            else sumRat++;
        }
        else if (countOpp == 2 && countFig == 0) sumRat = 0;
        else if (countOpp == 2 && countFig == 1) sumRat += 50;

        //проверить строки
        countFig = 0;
        countOpp = 0;
        s.get(j+3).set(i,figure);
        for (Character a: s.get(j+3))
            if (a.equals(figure)) countFig++;
            else if(a.equals(opponent)) countOpp++;
        if (countOpp == 0) {
            if (countFig == 3) return new int[]{100, i, j};
            else if (countFig == 2) sumRat += 2;
            else sumRat++;
        }
        else if (countOpp == 2 && countFig == 0) sumRat = 0;
        else if (countOpp == 2 && countFig == 1) sumRat += 50;

        //если принадлежит первой диагонали, проверить ее
        if (i == 0 && j == 0 || i == 1 && j == 1 || i == 2 && j == 2) {
            countFig = 0;
            countOpp = 0;
            s.get(6).set(i,figure);
            for (Character a: s.get(6))
                if (a.equals(figure)) countFig++;
                else if(a.equals(opponent)) countOpp++;
            if (countOpp == 0) {
                if (countFig == 3) return new int[]{100, i, j};
                else if (countFig == 2) sumRat += 2;
                else sumRat++;
            }
            else if (countOpp == 2 && countFig == 0) sumRat = 0;
            else if (countOpp == 2 && countFig == 1) sumRat += 50;
        }

        //если принадлежит второй диагонали, проверить ее
        if (i == 2 && j == 0 || i == 1 && j == 1 || i == 0 && j == 2) {
            countFig = 0;
            countOpp = 0;
            s.get(7).set(j,figure);
            for (Character a: s.get(7))
                if (a.equals(figure)) countFig++;
                else if(a.equals(opponent)) countOpp++;
            if (countOpp == 0) {
                if (countFig == 3) return new int[]{100, i, j};
                else if (countFig == 2) sumRat += 2;
                else sumRat++;
            }
            else if (countOpp == 2 && countFig == 0) sumRat = 0;
            else if (countOpp == 2 && countFig == 1) sumRat += 50;
        }

        return new int[] {sumRat,i,j};
    }

    public ArrayList<ArrayList<Character>> getLineRowAndDiagonals(char[][] s) {
        //первые три - столбцы, следующий три - строки, последние две - диагонали (/ -> \)
        ArrayList<ArrayList<Character>> linRowAndDiagonals = new ArrayList<ArrayList<Character>>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                linRowAndDiagonals.add(new ArrayList<Character>());
            }
        }
        linRowAndDiagonals.add(new ArrayList<Character>());
        linRowAndDiagonals.add(new ArrayList<Character>());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              linRowAndDiagonals.get(i).add(new Character(s[i][j]));
              linRowAndDiagonals.get(i+3).add(new Character(s[j][i]));
            }
            linRowAndDiagonals.get(6).add(new Character(s[i][i]));
        }
        linRowAndDiagonals.get(7).add(new Character(s[2][0]));
        linRowAndDiagonals.get(7).add(new Character(s[1][1]));
        linRowAndDiagonals.get(7).add(new Character(s[0][2]));

        return linRowAndDiagonals;
    }

    public int[] checkPreLoose(ArrayList<ArrayList<Character>> s) {
        for (int i = 0; i < 8; i++) {
            int sumFig = 0, count = 0, myCount = 0;
            for (int j = 0; j < 3; j++) {
                if (s.get(i).get(j).equals(opponent)) {
                    int ind = j;
                    sumFig += ind;
                    count++;
                }
                if (s.get(i).get(j).equals(figure)) myCount++;
            }
            if (count == 2 && myCount == 0)
                if (sumFig == 1) return new int[] {i,2};
                else if (sumFig == 3) return new int[] {i,0};
                else if (sumFig == 2) return new int[] {i, 1};
        }
        return new int[] {-1,-1};
    }


    public class CompareArray implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            if (a[0] > b[0]) return 1;
            else if (a[0] < b[0]) return -1;
            else return 0;
        }
    }

    public char[][] copyArray(char [][] s) {
        char[][] arr = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) arr[i][j] = s[i][j];
        return arr;
    }
}
