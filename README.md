# traveling-salesman-problem-thesis
Algorithms for thesis connected to traveling salesman problem. 

### Algorytmy aproksymujące dla problemu komiwojażera

## Plan i schemat pracy:

1.	Wprowadzenie, przedstawienie oraz opis problemu + historia i geneza
2.	Przejście do analizy algorytmów. Implementacja, ocena złożoności. Porównanie działania algorytmów dla różnych rozmiarów danych wejściowych oraz różnych rodzajów grafów (symetryczny oraz asymetryczny problem komiwojażera).
3.	Podsumowanie oraz wnioski.


# Algorytmy, które udało mi się znaleźć:

## Algorytmy dokładne:

1.	Rozwiązanie naiwne, wszystkie możliwe permutacje
2.	Algorytm Bellmana-Helda-Karpa
3.	Algorytm Branch and Bound

## Algorytmy aproksymacyjne i heurystyczne:

4.	Algorytm najbliższego sąsiada
5.	Algorytm mrówkowy
6.	Algorytm genetyczny

## Metryczny problem komiwojażera, krawędzie grafu spełniają nierówność trójkąta:

7.	Heurystyka Lin - Khernighana
8.	Algorytmy k-optymalne, 2-opt, 3-opt…
9.	Algorytm Christofidesa, 3/2 aproksymacyjny
10.	 Algorytm 2 aproksymacyjny


----------------------------------------------------------------------------------------------

Aplikacja prezentujaca dzialanie:

    Mozliwe generowanie lokalizacji/miast:

    1. generowanie randomyowych wspolrzednych z przedzialu
    2. pobieranie wspolrzednych miast z API i przedstawienie na mapie
    3. sferyczny uklad wspolrzednych - mapa kosmosu, prezentacja na

Prezentacja/omowienie:
- omowienie zlozonosci oraz wymagan/zalozen algorytmow
- prezentacja dzialania algorytmow na mapie/wykresie

## todo 15-30.10

- [X] opis problemu komiwojażera
- [X] algorytm naiwny
    - [X] dokladny opis algorytmu
    - [X] implementacja
    - [X] analiza ograniczenie sprawdzania sciezek
    - [X] pseudo kod CLRS
    - [X] analiza złożoności czasowej/pamieciowej
- [X] przeglad, poukladanie, zredegaowanie
- [X] przeslanie do Pani Suwady

[geeksforgeeks - native implementation, (n-1)! perms](https://www.geeksforgeeks.org/traveling-salesman-problem-tsp-implementation/)

## todo 30.10 - 1.12.

- [X] podzial na asymetryczny i symetryczny problem
- [X] bellman held karp - implementacja
  - [X] impelemntacja
  - [X] opis 
  - [X] analiza zlozonosci
- [X] nierownosc trojkata w problemie komiwojazera
- [X] przeglad algorytmow + literatura
  - [X] posortowanie po algorytmow wedlug date powstania
  - [X] opis tych algorytmow
- [X] implementacja wybranych algorytmow z listy
  - [X] algorytm christofidesa
  - [ ] algorytm genetyczny
  - [ ] tabu search
- [X] implementacja wizualizacji
  - [X] widok punktow jako wspolrzedne (x, y) na mapie
  - [ ] jak zaimplementowac mechanizm do wyswietlania dzialania algorytmu
- [X] opisy algorytmow do pracy
- [X] zredagowac, poukladac
- [X] porownanie algorytmow -- wykresy
- [ ] rozdział z opisem aplikacji
- [ ] poprawki z pliku
- [ ] poprawki opisu testow 