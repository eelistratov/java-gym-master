package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        TrainingSession firstSession = mondaySchedule.firstEntry().getValue().get(0);
        Assertions.assertEquals(1, mondaySchedule.size());
        Assertions.assertEquals(singleTrainingSession, firstSession);

        //Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySchedule.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySchedule.size());
        Assertions.assertEquals(mondayChildTrainingSession, mondaySchedule.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        // Получаем все тренировки за четверг в порядке возрастания времени
        List<TrainingSession> thursdaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : thursdaySchedule.values()) {
            thursdaySessions.addAll(sessions);
        }

        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(13, thursdaySessions.get(0).getTimeOfDay().getHours());
        Assertions.assertEquals(0, thursdaySessions.get(0).getTimeOfDay().getMinutes());
        Assertions.assertEquals(20, thursdaySessions.get(1).getTimeOfDay().getHours());
        Assertions.assertEquals(0, thursdaySessions.get(1).getTimeOfDay().getMinutes());

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySchedule.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt13.size());
        Assertions.assertEquals(singleTrainingSession, sessionsAt13.get(0));

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertTrue(sessionsAt14.isEmpty());
    }

    //----------------------Тесты для getTrainingSessionsForDay

    //Проверка получения тренировок за день, когда в одно время проходит несколько тренировок
    @Test
    void testGetTrainingSessionsForDay_MultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        // Добавляем 3 тренировки в понедельник в 18:00
        TrainingSession session1 = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session3 = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        // Получаем все тренировки за понедельник
        List<TrainingSession> mondaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : mondaySchedule.values()) {
            mondaySessions.addAll(sessions);
        }
        // Проверяем, что все 3 тренировки вернулись и они в правильном порядке (по времени)
        Assertions.assertEquals(3, mondaySessions.size());
        Assertions.assertEquals(18, mondaySessions.get(0).getTimeOfDay().getHours());
        Assertions.assertEquals(18, mondaySessions.get(1).getTimeOfDay().getHours());
        Assertions.assertEquals(18, mondaySessions.get(2).getTimeOfDay().getHours());
    }

    //Проверка получения тренировок за день, когда тренировки есть во всех временных слотах
    @Test
    void testGetTrainingSessionsForDay_AllTimeSlots() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // Добавляем тренировки на каждые 3 часа в среду
        for (int hour = 0; hour < 24; hour += 3) {
            timetable.addNewTrainingSession(new TrainingSession(group, coach,
                    DayOfWeek.WEDNESDAY, new TimeOfDay(hour, 0)));
        }

        TreeMap<TimeOfDay, List<TrainingSession>> wednesdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);

        // Получаем все тренировки за среду
        List<TrainingSession> wednesdaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : wednesdaySchedule.values()) {
            wednesdaySessions.addAll(sessions);
        }

        // Проверяем, что вернулось 8 тренировок (24/3)
        Assertions.assertEquals(8, wednesdaySessions.size());

        // Проверяем, что они отсортированы по времени
        List<TimeOfDay> times = new ArrayList<>(wednesdaySchedule.keySet());
        for (int i = 0; i < times.size() - 1; i++) {
            int currentHour = times.get(i).getHours();
            int nextHour = times.get(i + 1).getHours();
            Assertions.assertTrue(currentHour < nextHour,
                    "Тренировки должны быть отсортированы по возрастанию времени");
        }
    }

    //Проверка получения тренировок за день, когда тренировки есть во все дни недели
    @Test
    void testGetTrainingSessionsForDay_AllDaysOfWeek() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // Добавляем по одной тренировке в каждый день
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.addNewTrainingSession(new TrainingSession(group, coach,
                    day, new TimeOfDay(10, 0)));
        }

        // Проверяем каждый день
        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.getTrainingSessionsForDay(day);

            // Получаем первую тренировку за день
            TrainingSession firstSession = daySchedule.firstEntry().getValue().get(0);
            Assertions.assertEquals(1, daySchedule.size(),
                    "В день " + day + " должна быть ровно одна тренировка");
            Assertions.assertEquals(day, firstSession.getDayOfWeek());
        }
    }

    //----------------------Тесты для getTrainingSessionsForDayAndTime

    //Проверка получения тренировок по времени, когда в указанное время проходит несколько тренировок
    @Test
    void testGetTrainingSessionsForDayAndTime_MultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        // Добавляем 3 тренировки в пятницу в 19:30
        TrainingSession session1 = new TrainingSession(groupChild, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 30));
        TrainingSession session2 = new TrainingSession(groupAdult, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 30));
        TrainingSession session3 = new TrainingSession(groupChild, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 30));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<TrainingSession> sessionsAt1930 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.FRIDAY, new TimeOfDay(19, 30));

        Assertions.assertEquals(3, sessionsAt1930.size());
        Assertions.assertTrue(sessionsAt1930.contains(session1));
        Assertions.assertTrue(sessionsAt1930.contains(session2));
        Assertions.assertTrue(sessionsAt1930.contains(session3));
    }

    //Проверка получения тренировок по времени для граничных значений 00:00 и 23:59
    @Test
    void testGetTrainingSessionsForDayAndTime_BoundaryValues() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // Добавляем тренировки в граничное время
        TrainingSession midnightSession = new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(0, 0));
        TrainingSession endOfDaySession = new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(23, 59));

        timetable.addNewTrainingSession(midnightSession);
        timetable.addNewTrainingSession(endOfDaySession);

        // Проверяем получение в 00:00
        List<TrainingSession> sessionsAtMidnight = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SATURDAY, new TimeOfDay(0, 0));
        Assertions.assertEquals(1, sessionsAtMidnight.size());
        Assertions.assertEquals(midnightSession, sessionsAtMidnight.get(0));

        // Проверяем получение в 23:59
        List<TrainingSession> sessionsAtEndOfDay = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SATURDAY, new TimeOfDay(23, 59));
        Assertions.assertEquals(1, sessionsAtEndOfDay.size());
        Assertions.assertEquals(endOfDaySession, sessionsAtEndOfDay.get(0));

        // Проверяем, что в соседнее время тренировок нет
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SATURDAY, new TimeOfDay(0, 1)).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SATURDAY, new TimeOfDay(23, 58)).isEmpty());
    }

    //Проверка получения тренировок по времени для разных дней с одинаковым временем
    @Test
    void testGetTrainingSessionsForDayAndTime_SameTimeDifferentDays() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // Добавляем тренировки в одно и то же время (15:00) в разные дни
        TrainingSession mondaySession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        TrainingSession wednesdaySession = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));
        TrainingSession fridaySession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(wednesdaySession);
        timetable.addNewTrainingSession(fridaySession);

        // Проверяем, что в понедельник в 15:00 есть тренировка
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(mondaySession, mondaySessions.get(0));

        // Проверяем, что во вторник в 15:00 тренировок нет
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.TUESDAY, new TimeOfDay(15, 0)).isEmpty());

        // Проверяем, что в среду в 15:00 есть тренировка
        List<TrainingSession> wednesdaySessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));
        Assertions.assertEquals(1, wednesdaySessions.size());
        Assertions.assertEquals(wednesdaySession, wednesdaySessions.get(0));
    }

    //----------------------Тесты для getCountByCoaches

    //Проверка подсчёта тренировок для одного тренера
    @Test
    void testGetCountByCoaches_SingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // Добавляем 5 тренировок для одного тренера
        for (int i = 0; i < 5; i++) {
            DayOfWeek day = DayOfWeek.values()[i % 7];
            timetable.addNewTrainingSession(new TrainingSession(group, coach,
                    day, new TimeOfDay(10 + i, 0)));
        }

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(coach, result.get(0).getCoach());
        Assertions.assertEquals(5, result.get(0).getCount());
    }

    //Проверка подсчёта и сортировки для нескольких тренеров с разным количеством тренировок
    @Test
    void testGetCountByCoaches_MultipleCoachesWithDifferentCounts() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // coach1: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        // coach2: 4 тренировки (должен быть первым)
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(11, 0)));

        // coach3: 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.FRIDAY, new TimeOfDay(12, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(3, result.size());

        // Проверяем сортировку по убыванию
        Assertions.assertEquals(4, result.get(0).getCount());
        Assertions.assertEquals(3, result.get(1).getCount());
        Assertions.assertEquals(2, result.get(2).getCount());

        // Проверяем, что это правильные тренеры
        Assertions.assertEquals(coach2, result.get(0).getCoach());
        Assertions.assertEquals(coach3, result.get(1).getCoach());
        Assertions.assertEquals(coach1, result.get(2).getCoach());
    }

    //Проверка подсчёта при наличии нескольких тренировок в одно время и с одинаковым количеством у разных тренеров
    @Test
    void testGetCountByCoaches_EqualCountsAndSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        // У всех тренеров по 2 тренировки
        // Иванов: 2 тренировки (одна в одно время с Петровым)
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0))); // Одно время с Петровым
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));

        // Петров: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0))); // Одно время с Ивановым
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        // Сидоров: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(3, result.size());

        // Проверяем, что у всех по 2 тренировки
        for (CounterOfTrainings stats : result) {
            Assertions.assertEquals(2, stats.getCount());
        }

        // Проверяем, что все три тренера присутствуют
        Set<Coach> coaches = new HashSet<>();
        for (CounterOfTrainings stats : result) {
            coaches.add(stats.getCoach());
        }
        Assertions.assertTrue(coaches.contains(coach1));
        Assertions.assertTrue(coaches.contains(coach2));
        Assertions.assertTrue(coaches.contains(coach3));
    }
}
