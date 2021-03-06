# Синтаксис языка L

В языке L есть:
  - идентификаторы (состоят из строчных латинских букв, цифр и символа _; начинаются с _ или буквы)
  - бинарные операторы: `+`, `−`, `*`, `/`, `%`, `==`, `!=`, `>`, `>=`, `<`, `<=`, `&&`, `||`
  - определения функций
    - имена функций являются идентификаторами
    - аргументы функций являются идентификаторами
    - список аргументов функции может быть пустым
    - аргументы функций записываются в скобках, отделяются запятой, после запятой стоит произвольное количество пробельных символов
    - между именем функции и открывающей скобкой списка аргументов стоит произвольное количество пробельных символов
    - после списка аргументов начинается тело функции; между списком аргументов и телом функции стоит произвольное количество пробельных символов
    - тело функции заключено в одинарные скобки {}
 - вызовы функций
   - вызов функции состоит из имени функции, открывающей скобки, перечисления списка аргументов (произвольные выражения языка) через запятую, закрывающей скобки; все эти конструкции разделяются произвольным количеством пробельных символов
 - выражения
   - вызовы функций
   - идентификаторы
   - натуральные числа
   - результаты применения операторов к произвольным выражениям (оператор и выражения отделяются друг от друга произвольным количеством пробельных символов)
   - выражения могут быть заключены в скобки, между скобками и внутренним выражением стоит произвольное количество пробельных символов
 - операторы:
   - бинарные операторы
   - оператор присваивания
   - вызовы функций
   - `write (*выражение*)`
   - `read (*идентификатор*)`
   - между `write`/`read` и соответствующим аргументом стоит произвольное количество пробельных символов
   - внутри скобок находится только один аргумент, отделённый от скобок произвольным количеством пробельных символов
   - `while (*выражение*) do {*список операторов*}`
   - между `while` и (, ) и `do`, `do` и { стоит произвольное количество пробельных символов
   - `if (*выражение*) then {*список операторов*} else {*список операторов*}` (`else {...}` можно опустить, см. синтаксический сахар)
   - между `if` и (, ) и `then`, `then` и {, } и `else`, `else` и { стоит произвольное количество пробельных символов
   - `if` и `while` могут содержать только одно условие
   - условие внутри `if`/`while` и скобки отделяются произвольным количеством пробельных символов
   - последовательность из вышеперечисленных
   - почти все операторы разделяются точкой с запятой, после точки с запятой идёт произвольное количество пробельных символов
   - после } не требуется точка с запятой
   - точка с запятой не ставится, если оператор является аргументом функции, условием `while` или `if`
 - программа
   - первыми перечислены определения функций, разделённые произвольным количеством пробельных символов
   - затем идёт список операторов, разделённых точкой с запятой
 - отступы в начале строки не имеют значения, пробельные символы в конце строки (после точки с запятой или }) не имеют значения   
 - к пробельным символам относятся: пробел, `\t`, `\n`, `\r`, `\r\n`, `\f` 

# Синтаксический сахар
 - операторы `+=`, `-=`, `*=`, `/=`, `%=` (`a = a + 1` эквивалентно `a += 1`)
 - постфиксные инкремент и декремент (`a++` эквивалентно `a := a + 1`)
 - можно опустить `else` в выражении `if (...) then {...} else {...}`, это эквивалентно выражению `if (...) then {...} else {}`
# Пример кода

    function1(arg1, arg2, _arg3)
    {
      x := arg1;
      y := arg2 - _arg3;
      write (x / y);
    }
    
    function2()
    {
      if (3 > 5)
      then 
      {
        read (x);
      }
      else 
      {
      read (y);
      write (function1(x, y, x - 2 * y));
      }
    }
    
    read (x);
    read (y);
    read (z);
    function1(x, y, z);
    function2();
    k := x * y - 2;
    if (k % 3 == 0)
    then
    {
    function1(k, x, y);
    }
    else
    {
    function2();
    }
    k := 16;
    while (k > 10)
    do
    {
    write (function1(k, k, k));
    k := k - 1;
    }
 

