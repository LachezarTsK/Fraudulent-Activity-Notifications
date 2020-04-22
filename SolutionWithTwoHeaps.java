import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class SolutionWithTwoHeaps {
  private static PriorityQueue<Integer> lowerValues;
  private static PriorityQueue<Integer> higherValues;

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    int days_withTransactionData = scanner.nextInt();
    int trailingDays = scanner.nextInt();
    int[] expenditure = new int[days_withTransactionData];

    for (int i = 0; i < days_withTransactionData; i++) {
      expenditure[i] = scanner.nextInt();
    }
    scanner.close();

    int total_notifications = find_numberOfNotifications(trailingDays, expenditure);
    System.out.println(total_notifications);
  }

  /**
   * Finds the number of issued notifications for the period encompassing 
   * the days with transaction data (days_withTransactionData). 
   *
   * Notification is issued each time when the expenditure for a given day exceeds 
   * the median expenditure for the specified previos number of days (trailingDays).
   *
   * @return An integer, representing the number of issued notifications.
   */
  private static int find_numberOfNotifications(int trailingDays, int[] expenditure) {

    lowerValues = new PriorityQueue<Integer>(priority_maximumValue());
    higherValues = new PriorityQueue<Integer>();
    for (int i = 0; i < trailingDays; i++) {
      addValue_toPriorityQueues(expenditure[i]);
    }

    double median = findMedian();
    int notifications = expenditure[trailingDays] >= 2 * median ? 1 : 0;

    int index_oldestTrailingElement = 0;
    for (int i = trailingDays; i < expenditure.length - 1; i++) {

      removeValue_fromPriorityQueues(expenditure[index_oldestTrailingElement]);
      addValue_toPriorityQueues(expenditure[i]);
      index_oldestTrailingElement++;

      median = findMedian();
      notifications = expenditure[i + 1] >= 2 * median ? notifications + 1 : notifications;
    }

    return notifications;
  }

  private static Comparator<Integer> priority_maximumValue() {
    Comparator<Integer> maximumFirst =
        new Comparator<Integer>() {
          @Override
          public int compare(Integer valueOne, Integer valueTwo) {
            return Integer.compare(valueTwo, valueOne);
          }
        };
    return maximumFirst;
  }

  private static void addValue_toPriorityQueues(int value) {
    if (lowerValues.size() == 0 || lowerValues.peek() >= value) {
      lowerValues.add(value);
    } else {
      higherValues.add(value);
    }
    balancePriorityQueues();
  }

  private static void removeValue_fromPriorityQueues(int value) {
    if (lowerValues.size() > 0 && lowerValues.peek() >= value) {
      lowerValues.remove(value);
    } else if (higherValues.size() > 0) {
      higherValues.remove(value);
    }
    balancePriorityQueues();
  }

  private static void balancePriorityQueues() {
    if (1 + lowerValues.size() < higherValues.size()) {
      lowerValues.add(higherValues.poll());
    } else if (1 + higherValues.size() < lowerValues.size()) {
      higherValues.add(lowerValues.poll());
    }
  }

  private static double findMedian() {
    if (lowerValues.size() == 0 && higherValues.size() == 0) {
      return 0;
    }
    return lowerValues.size() == higherValues.size()
        ? (double) (lowerValues.peek() + higherValues.peek()) / 2
        : lowerValues.size() > higherValues.size() ? lowerValues.peek() : higherValues.peek();
  }
}
