Laboratorium III - JPQL

Uwaga! Do wykonania zadan konieczne jest zaimplementowanie architektury warstwowej i testow z Laboratorium II !

Uzupelnij plik data.sql o dane niezbedne do realizacji nastepujacych zapytan:
1. Znajdz pacjentow po nazwisku
2. Znajdz wszystkie wizyty pacjenta po jego ID
3. znajdz pacjentow ktorzy mieli wiecej niz X wizyt (X jest parametrem wejsciowym)
4. Znajdz pacjentow po dodanym przez Ciebie polu - nie wyszukuj wprost po wartosci, uzyj zapytania typu wieksze/mniejsze/pozniej/wczesniej/zawiera, w zaleznosci od wybranego typu zmiennej.

Napisz testy do zapytan w nastepujacej formie:
1. do zapytania nr 1  - test DAO
2. do zapytania nr 2 - test serwisu
3. do zapytania nr 3 - test DAO
4. do zapytania nr 4 - test DAO

W PatientEntity, nad relacja do VisitEntity dodaj adnotacje

@Fetch(FetchMode.SELECT)

a fetchType zmien na EAGER
Uruchom test w ktorym pobierany jest Patient z wieloma wizytami. W logach zaobserwuj, jak wyglada pobieranie dodatkowych encji (ile i jakie sqle).
Nastepnie zmien adnotacje na

@Fetch(FetchMode.JOIN)

i powtorz test i obserwacje. Wnioski zapisz na dole tego pliku i skomituj.

Do wybranej encji dodaj wersjonowanie, oraz napisz test (w DAO) sprawdzajacy rownolegla modyfikacje (OptimisticLock)

Wnioski z testów strategii pobierania:
Analizując zachowanie różnych strategii pobierania danych, zauważyłem znaczące różnice między FetchMode.SELECT a FetchMode.JOIN.
W przypadku SELECT widziałem w logach, że Hibernate wykonuje najpierw zapytanie o główną encję (pacjenta), a dopiero potem
osobne zapytanie o powiązane wizyty. Z jednej strony generuje to więcej zapytań do bazy, ale z drugiej może być sensowne
gdy nie zawsze potrzebujemy tych powiązanych danych. Natomiast przy użyciu JOIN wszystko pobierane jest za jednym razem
w logach widać jedno, większe zapytanie ze złączeniem tabel. Jest to zazwyczaj wydajniejsze, bo redukuje liczbę połączeń
z bazą, ale zauważyłem też, że ilość pobieranych danych jest większa. 
Po przeprowadzonych testach mogę stwierdzić, że wybór między tymi strategiami powinien zależeć od konkretnego przypadku użycia
czy częściej będziemy potrzebować pełnych danych (wtedy JOIN), czy może czasem wystarczy nam sama encja główna (wtedy 
SELECT może być lepszym wyborem).