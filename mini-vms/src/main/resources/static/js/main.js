/* =========================================================================
 * MiniVMS - Shared Client Script
 * Single JS file for the entire application. No jQuery required.
 * Responsibilities:
 *   1. Registration form: toggle "Work Order ID" field when Role = Worker
 *   2. Account dropdown open/close behavior (Bootstrap dropdown enhancement)
 *   3. Simple client-side form validation (required, email, password match)
 *   4. Bootstrap modal trigger helpers for confirmation dialogs
 *   5. Small fetch() helper for future AJAX enhancements
 * ========================================================================= */
(function () {
  "use strict";

  /* ----------------------------------------------------------------- */
  /* Utilities                                                          */
  /* ----------------------------------------------------------------- */
  function qs(selector, scope) {
    return (scope || document).querySelector(selector);
  }

  function qsa(selector, scope) {
    return Array.prototype.slice.call((scope || document).querySelectorAll(selector));
  }

  function onReady(fn) {
    if (document.readyState === "loading") {
      document.addEventListener("DOMContentLoaded", fn);
    } else {
      fn();
    }
  }

  var EMAIL_PATTERN = /^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z]+(-[A-Za-z]+)*(\.[A-Za-z]+(-[A-Za-z]+)*)+$/;

  /* ----------------------------------------------------------------- */
  /* 1. Registration: toggle Work Order ID field for Worker role        */
  /* ----------------------------------------------------------------- */
  function initRoleToggle() {
    var roleSelect = qs("#role");
    var workOrderGroup = qs("#work-order-id-group");
    if (!roleSelect || !workOrderGroup) {
      return;
    }

    function toggle() {
      var isWorker = roleSelect.value === "WORKER";
      workOrderGroup.classList.toggle("d-none", !isWorker);
      var input = qs("#workOrderId", workOrderGroup);
      if (input) {
        input.required = isWorker;
        if (!isWorker) {
          input.value = "";
          input.classList.remove("is-invalid");
        }
      }
    }

    roleSelect.addEventListener("change", toggle);
    toggle();
  }

  /* ----------------------------------------------------------------- */
  /* 2. Account dropdown behavior                                       */
  /* Bootstrap's dropdown JS (bootstrap.bundle.min.js) already handles  */
  /* open/close + aria-expanded toggling. This adds a safety net for    */
  /* closing the dropdown when clicking outside, and keyboard escape.   */
  /* ----------------------------------------------------------------- */
  function initAccountDropdown() {
    var toggleEl = qs("#account-dropdown-toggle");
    if (!toggleEl) {
      return;
    }

    document.addEventListener("keydown", function (evt) {
      if (evt.key === "Escape") {
        var expanded = toggleEl.getAttribute("aria-expanded") === "true";
        if (expanded && window.bootstrap) {
          var instance = window.bootstrap.Dropdown.getOrCreateInstance(toggleEl);
          instance.hide();
        }
      }
    });
  }

  /* ----------------------------------------------------------------- */
  /* 3. Simple client-side form validation                              */
  /* ----------------------------------------------------------------- */
  function showFieldError(field, message) {
    field.classList.add("is-invalid");
    var feedback = field.parentElement.querySelector(".invalid-feedback");
    if (feedback && message) {
      // adding if statement so the error message from the DTO are displaye
      if (!feedback.textContent) {
        feedback.textContent = message;
      }

    }
  }

  function clearFieldError(field) {
    field.classList.remove("is-invalid");
  }

  function validateRequired(form) {
    var valid = true;
    qsa("[required]", form).forEach(function (field) {
      if (field.type === "checkbox") {
        if (!field.checked) {
          showFieldError(field, "This field is required.");
          valid = false;
        } else {
          clearFieldError(field);
        }
        return;
      }
      if (!field.value || !field.value.trim()) {
        showFieldError(field, "This field is required.");
        valid = false;
      } else {
        clearFieldError(field);
      }
    });
    return valid;
  }

  function validateEmails(form) {
    var valid = true;
    qsa('input[type="email"]', form).forEach(function (field) {
      if (field.value && !EMAIL_PATTERN.test(field.value)) {
        showFieldError(field, "Enter a valid email address.");
        valid = false;
      } else if (field.value) {
        clearFieldError(field);
      }
    });
    return valid;
  }

  function validatePasswordMatch(form) {
    var password = qs("#password", form);
    var confirm = qs("#confirmPassword", form);
    if (!password || !confirm) {
      return true;
    }
    if (password.value !== confirm.value) {
      showFieldError(confirm, "Passwords do not match.");
      return false;
    }
    clearFieldError(confirm);
    return true;
  }

  function showTopLevelError(form, message) {
    var alertBox = form.querySelector(".form-error-alert");
    if (!alertBox) {
      return;
    }
    if (message) {
      alertBox.textContent = message;
      alertBox.classList.remove("d-none");
    } else {
      alertBox.classList.add("d-none");
      alertBox.textContent = "";
    }
  }

  function initFormValidation() {
    qsa("form[data-validate]").forEach(function (form) {
      form.addEventListener("submit", function (evt) {
        var requiredOk = validateRequired(form);
        var emailOk = validateEmails(form);
        var passwordOk = validatePasswordMatch(form);

        if (!requiredOk || !emailOk || !passwordOk) {
          evt.preventDefault();
          evt.stopPropagation();
          showTopLevelError(form, "Please fix the highlighted fields before submitting.");
        } else {
          showTopLevelError(form, null);
        }
        form.classList.add("was-validated");
      });

      // Clear individual field errors as the user types/selects.
      qsa("input, select, textarea", form).forEach(function (field) {
        field.addEventListener("input", function () {
          if (field.value && field.value.trim()) {
            clearFieldError(field);
          }
        });
      });
    });
  }

  /* ----------------------------------------------------------------- */
  /* 4. Confirmation modal triggers for destructive actions             */
  /* Usage: <button data-confirm-modal="#confirmDeleteModal"            */
  /*         data-confirm-target-form="deleteForm">Delete</button>      */
  /* ----------------------------------------------------------------- */
  function initConfirmModals() {
    qsa("[data-confirm-modal]").forEach(function (trigger) {
      trigger.addEventListener("click", function (evt) {
        var modalSelector = trigger.getAttribute("data-confirm-modal");
        var modalEl = qs(modalSelector);
        if (!modalEl || !window.bootstrap) {
          return;
        }

        evt.preventDefault();

        var confirmBtn = qs("[data-confirm-action]", modalEl);
        var modal = window.bootstrap.Modal.getOrCreateInstance(modalEl);

        if (confirmBtn) {
          var handler = function () {
            var formId = trigger.getAttribute("data-confirm-target-form");
            if (formId) {
              var form = document.getElementById(formId);
              if (form) {
                form.submit();
              }
            } else if (trigger.tagName === "A" && trigger.href) {
              window.location.href = trigger.href;
            }
            confirmBtn.removeEventListener("click", handler);
            modal.hide();
          };
          confirmBtn.addEventListener("click", handler);
        }
        modal.show();
      });
    });
  }

  /* ----------------------------------------------------------------- */
  /* 5. Timesheet hour helpers: per-day and weekly total validation     */
  /* ----------------------------------------------------------------- */
  var DAILY_HOUR_LIMIT = 24;
  var WEEKLY_HOUR_LIMIT = 168;

  function initTimesheetValidation() {
    var table = qs(".timesheet-table[data-editable]");
    if (!table) {
      return;
    }
    var dayInputs = qsa(".day-input", table);
    var totalEl = qs("#weekly-total");

    function recalcTotal() {
      var total = 0;
      dayInputs.forEach(function (input) {
        var val = parseFloat(input.value);
        if (isNaN(val) || val < 0) {
          val = 0;
        }
        if (val > DAILY_HOUR_LIMIT) {
          showFieldError(input, "Max " + DAILY_HOUR_LIMIT + " hours per day.");
          val = DAILY_HOUR_LIMIT;
        } else {
          clearFieldError(input);
        }
        total += val;
      });

      if (totalEl) {
        totalEl.textContent = total.toFixed(2);
        totalEl.classList.toggle("text-danger", total > WEEKLY_HOUR_LIMIT);
      }
    }

    dayInputs.forEach(function (input) {
      input.addEventListener("input", recalcTotal);
    });
    recalcTotal();
  }

  /* ----------------------------------------------------------------- */
  /* 6. Small fetch() helper for future AJAX enhancements               */
  /* ----------------------------------------------------------------- */
  window.MiniVMS = window.MiniVMS || {};
  window.MiniVMS.ajax = {
    getJson: function (url) {
      return fetch(url, {
        headers: { Accept: "application/json" },
        credentials: "same-origin"
      }).then(function (res) {
        if (!res.ok) {
          throw new Error("Request failed: " + res.status);
        }
        return res.json();
      });
    },
    postJson: function (url, data) {
      return fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify(data)
      }).then(function (res) {
        if (!res.ok) {
          throw new Error("Request failed: " + res.status);
        }
        return res.json();
      });
    }
  };

  /* ----------------------------------------------------------------- */
  /* Init                                                                */
  /* ----------------------------------------------------------------- */
  onReady(function () {
    initRoleToggle();
    initAccountDropdown();
    initFormValidation();
    initConfirmModals();
    initTimesheetValidation();
  });
})();
