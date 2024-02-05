### Algorytmy aproksymujące dla problemu komiwojażera

## Algorytmy dokładne:

1.	Rozwiązanie naiwne, wszystkie możliwe permutacje
2.	Algorytm Bellmana-Helda-Karpa

## Algorytmy aproksymacyjne i heurystyczne:

3.	Algorytm najbliższego sąsiada
4.	Algorytmy k-optymalne, 2-opt, 3-opt…
5.	Algorytm 2 aproksymacyjny
6.	Algorytm Christofidesa, 3/2 aproksymacyjny

## Algorytm metaheurystyczny:

7. Algorytm mrówkowy

## Plan i schemat pracy:

1.	Wprowadzenie, przedstawienie oraz opis problemu + historia i geneza
2.	Przejście do analizy algorytmów. Implementacja, ocena złożoności. Porównanie działania algorytmów dla różnych rozmiarów danych wejściowych oraz różnych rodzajów grafów (symetryczny oraz asymetryczny problem komiwojażera).
3.	Podsumowanie oraz wnioski.


----------------------------------------------------------------------------------------------

Aplikacja prezentujaca dzialanie:

    Mozliwe generowanie lokalizacji/miast:

    1. generowanie randomyowych wspolrzednych z przedzialu
    2. pobieranie wspolrzednych miast z API i przedstawienie na mapie

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
- [X] implementacja wizualizacji
  - [X] widok punktow jako wspolrzedne (x, y) na mapie
- [X] opisy algorytmow do pracy
- [X] zredagowac, poukladac
- [X] porownanie algorytmow -- wykresy
- [X] poprawki z pliku
- [X] rozdział z opisem aplikacji
- [X] poprawki opisu testow 


## opcjonalnie


- [ ] jak zaimplementowac mechanizm do wyswietlania dzialania algorytmu
  - [ ] dodac rozwiązywanie na innym wątku
- [ ] lin kernighan
- [ ] algorytm genetyczny
- [ ] tabu search
- [ ] przetlumaczyc na jezyk angielski + opis i zredagowac cale readme
