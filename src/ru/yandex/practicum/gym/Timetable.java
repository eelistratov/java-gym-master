package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    //Конструктор создаёт пустое расписание
    public Timetable() {
        this.timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>(
                    Comparator.comparingInt(TimeOfDay::getHours)
                            .thenComparingInt(TimeOfDay::getMinutes)
            ));
        }
    }

    //Добавить новую тренировку в расписание
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        List<TrainingSession> sessionsAtTime = daySchedule.computeIfAbsent(
                time,
                k -> new ArrayList<>()
        );

        sessionsAtTime.add(trainingSession);
    }

    //Получить расписание на день недели
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }

        return result; // O(n) - количество тренировок за день
    }

    //Получить расписание по указанному времени
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>(); // O(1)
    }

    //Количество тренировок каждого тренера за неделю
    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        return result;
    }

}
